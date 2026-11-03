package com.pragma.powerup.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.powerup.application.dto.request.OrderTraceabilityRequestDto;
import com.pragma.powerup.application.dto.response.OrderTraceabilityResponseDto;
import com.pragma.powerup.application.handler.IOrderTraceabilityHandler;
import com.pragma.powerup.domain.exception.TraceabilityOrderNotBelongToClientException;
import com.pragma.powerup.infrastructure.exceptionhandler.ControllerAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderTraceabilityRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IOrderTraceabilityHandler traceabilityHandler;

    @InjectMocks
    private OrderTraceabilityRestController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Debe responder 201 Created al registrar un log de trazabilidad")
    void createLog_whenValid_shouldReturn201() throws Exception {
        OrderTraceabilityRequestDto requestDto = new OrderTraceabilityRequestDto(
                10L, 5L, "cliente@correo.com", LocalDateTime.now(), "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com"
        );
        OrderTraceabilityResponseDto responseDto = new OrderTraceabilityResponseDto(
                "log123", 10L, 5L, "cliente@correo.com", LocalDateTime.now(), "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com"
        );

        when(traceabilityHandler.createLog(any(OrderTraceabilityRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/traceability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("log123"))
                .andExpect(jsonPath("$.idPedido").value(10L))
                .andExpect(jsonPath("$.nuevoEstado").value("EN_PREPARACION"));
    }

    @Test
    @DisplayName("Debe responder 200 OK al consultar logs por ID de pedido")
    void getLogsByOrderId_whenValid_shouldReturn200() throws Exception {
        OrderTraceabilityResponseDto responseDto = new OrderTraceabilityResponseDto(
                "log123", 10L, 5L, "cliente@correo.com", LocalDateTime.now(), "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com"
        );

        when(traceabilityHandler.getLogsByOrderId(10L)).thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/v1/traceability/order/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPedido").value(10L));
    }

    @Test
    @DisplayName("Debe responder 403 Forbidden cuando el cliente no es el dueño del pedido consultado")
    void getLogsByOrderId_whenNotOwner_shouldReturn403() throws Exception {
        when(traceabilityHandler.getLogsByOrderId(10L))
                .thenThrow(new TraceabilityOrderNotBelongToClientException());

        mockMvc.perform(get("/api/v1/traceability/order/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Solo puedes consultar el historial de logs de tus propios pedidos."));
    }

    @Test
    @DisplayName("Debe responder 200 OK al consultar historial del cliente autenticado")
    void getLogsForCurrentCustomer_whenValid_shouldReturn200() throws Exception {
        OrderTraceabilityResponseDto responseDto = new OrderTraceabilityResponseDto(
                "log123", 10L, 5L, "cliente@correo.com", LocalDateTime.now(), "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com"
        );

        when(traceabilityHandler.getLogsForCurrentCustomer()).thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/v1/traceability/customer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(5L));
    }
}
