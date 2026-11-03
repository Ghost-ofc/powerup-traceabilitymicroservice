package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderTraceabilityRequestDto;
import com.pragma.powerup.application.dto.response.OrderTraceabilityResponseDto;
import com.pragma.powerup.application.handler.impl.OrderTraceabilityHandler;
import com.pragma.powerup.application.mapper.IOrderTraceabilityRequestMapper;
import com.pragma.powerup.application.mapper.IOrderTraceabilityResponseMapper;
import com.pragma.powerup.domain.api.IOrderTraceabilityServicePort;
import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTraceabilityHandlerTest {

    @Mock
    private IOrderTraceabilityServicePort servicePort;

    @Mock
    private IOrderTraceabilityRequestMapper requestMapper;

    @Mock
    private IOrderTraceabilityResponseMapper responseMapper;

    @InjectMocks
    private OrderTraceabilityHandler handler;

    @Test
    @DisplayName("Debe delegar la creación de log al servicio de dominio y mapear a response DTO")
    void createLog_shouldDelegateToServiceAndMap() {
        OrderTraceabilityRequestDto requestDto = new OrderTraceabilityRequestDto(
                10L, 5L, "cliente@correo.com", LocalDateTime.now(), "PENDIENTE", "EN_PREPARACION", 20L, "empleado@correo.com"
        );
        OrderTraceabilityModel model = new OrderTraceabilityModel();
        OrderTraceabilityModel savedModel = new OrderTraceabilityModel();
        OrderTraceabilityResponseDto responseDto = new OrderTraceabilityResponseDto();

        when(requestMapper.toModel(requestDto)).thenReturn(model);
        when(servicePort.createLog(model)).thenReturn(savedModel);
        when(responseMapper.toResponse(savedModel)).thenReturn(responseDto);

        OrderTraceabilityResponseDto result = handler.createLog(requestDto);

        assertNotNull(result);
        verify(requestMapper).toModel(requestDto);
        verify(servicePort).createLog(model);
        verify(responseMapper).toResponse(savedModel);
    }

    @Test
    @DisplayName("Debe delegar la consulta por ID de pedido y mapear lista a response DTOs")
    void getLogsByOrderId_shouldDelegateToServiceAndMap() {
        OrderTraceabilityModel model = new OrderTraceabilityModel();
        OrderTraceabilityResponseDto responseDto = new OrderTraceabilityResponseDto();

        when(servicePort.getLogsByOrderId(10L)).thenReturn(Arrays.asList(model));
        when(responseMapper.toResponseList(Arrays.asList(model))).thenReturn(Arrays.asList(responseDto));

        List<OrderTraceabilityResponseDto> result = handler.getLogsByOrderId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(servicePort).getLogsByOrderId(10L);
        verify(responseMapper).toResponseList(Arrays.asList(model));
    }

    @Test
    @DisplayName("Debe delegar la consulta del cliente actual y mapear a response DTOs")
    void getLogsForCurrentCustomer_shouldDelegateToServiceAndMap() {
        OrderTraceabilityModel model = new OrderTraceabilityModel();
        OrderTraceabilityResponseDto responseDto = new OrderTraceabilityResponseDto();

        when(servicePort.getLogsForCurrentCustomer()).thenReturn(Arrays.asList(model));
        when(responseMapper.toResponseList(Arrays.asList(model))).thenReturn(Arrays.asList(responseDto));

        List<OrderTraceabilityResponseDto> result = handler.getLogsForCurrentCustomer();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(servicePort).getLogsForCurrentCustomer();
        verify(responseMapper).toResponseList(Arrays.asList(model));
    }
}
