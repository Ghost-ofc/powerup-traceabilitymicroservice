package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.RequiredFieldException;
import com.pragma.powerup.domain.exception.TraceabilityOrderNotBelongToClientException;
import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import com.pragma.powerup.domain.spi.IOrderTraceabilityPersistencePort;
import com.pragma.powerup.domain.spi.ISecurityContextPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTraceabilityUseCaseTest {

    @Mock
    private IOrderTraceabilityPersistencePort persistencePort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private OrderTraceabilityUseCase traceabilityUseCase;

    private OrderTraceabilityModel validLog;

    @BeforeEach
    void setUp() {
        validLog = new OrderTraceabilityModel(
                "log123",
                10L,
                5L,
                "cliente@correo.com",
                LocalDateTime.now(),
                "PENDIENTE",
                "EN_PREPARACION",
                20L,
                "empleado@correo.com"
        );
    }

    @Test
    @DisplayName("Debe registrar el log de trazabilidad exitosamente cuando los campos son válidos")
    void createLog_whenValidData_shouldSaveAndReturn() {
        when(persistencePort.saveLog(any(OrderTraceabilityModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderTraceabilityModel result = traceabilityUseCase.createLog(validLog);

        assertNotNull(result);
        assertEquals(10L, result.getIdPedido());
        assertEquals("EN_PREPARACION", result.getNuevoEstado());
        verify(persistencePort).saveLog(validLog);
    }

    @Test
    @DisplayName("Debe lanzar RequiredFieldException si el log es nulo al crearlo")
    void createLog_whenNullModel_shouldThrowException() {
        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.createLog(null));
    }

    @Test
    @DisplayName("Debe lanzar RequiredFieldException si falta idPedido, idCliente o nuevoEstado")
    void createLog_whenMissingFields_shouldThrowException() {
        validLog.setIdPedido(null);
        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.createLog(validLog));

        validLog.setIdPedido(10L);
        validLog.setIdCliente(null);
        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.createLog(validLog));

        validLog.setIdCliente(5L);
        validLog.setNuevoEstado(null);
        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.createLog(validLog));
    }

    @Test
    @DisplayName("Debe permitir consultar los logs de un pedido si el cliente autenticado es el dueño")
    void getLogsByOrderId_whenClientIsOwner_shouldReturnLogs() {
        when(persistencePort.getLogsByOrderId(10L)).thenReturn(Arrays.asList(validLog));
        when(securityContextPort.getCurrentUserId()).thenReturn(5L);

        List<OrderTraceabilityModel> result = traceabilityUseCase.getLogsByOrderId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getIdPedido());
        verify(persistencePort).getLogsByOrderId(10L);
    }

    @Test
    @DisplayName("Debe lanzar TraceabilityOrderNotBelongToClientException si el cliente autenticado no es el dueño del pedido")
    void getLogsByOrderId_whenClientIsNotOwner_shouldThrowException() {
        when(persistencePort.getLogsByOrderId(10L)).thenReturn(Arrays.asList(validLog));
        when(securityContextPort.getCurrentUserId()).thenReturn(99L); // Otro cliente

        assertThrows(TraceabilityOrderNotBelongToClientException.class,
                () -> traceabilityUseCase.getLogsByOrderId(10L));
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay logs para el pedido")
    void getLogsByOrderId_whenNoLogs_shouldReturnEmptyList() {
        when(persistencePort.getLogsByOrderId(999L)).thenReturn(Collections.emptyList());

        List<OrderTraceabilityModel> result = traceabilityUseCase.getLogsByOrderId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Debe lanzar RequiredFieldException si el orderId es nulo al consultar logs")
    void getLogsByOrderId_whenOrderIdNull_shouldThrowException() {
        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.getLogsByOrderId(null));
    }

    @Test
    @DisplayName("Debe retornar el historial de logs del cliente autenticado")
    void getLogsForCurrentCustomer_whenAuthenticated_shouldReturnList() {
        when(securityContextPort.getCurrentUserId()).thenReturn(5L);
        when(persistencePort.getLogsByCustomerId(5L)).thenReturn(Arrays.asList(validLog));

        List<OrderTraceabilityModel> result = traceabilityUseCase.getLogsForCurrentCustomer();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(persistencePort).getLogsByCustomerId(5L);
    }

    @Test
    @DisplayName("Debe lanzar RequiredFieldException si el cliente no está autenticado al consultar su historial")
    void getLogsForCurrentCustomer_whenNotAuthenticated_shouldThrowException() {
        when(securityContextPort.getCurrentUserId()).thenReturn(null);

        assertThrows(RequiredFieldException.class, () -> traceabilityUseCase.getLogsForCurrentCustomer());
        verify(persistencePort, never()).getLogsByCustomerId(any());
    }
}
