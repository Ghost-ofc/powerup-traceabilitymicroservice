package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderTraceabilityServicePort;
import com.pragma.powerup.domain.exception.RequiredFieldException;
import com.pragma.powerup.domain.exception.TraceabilityOrderNotBelongToClientException;
import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import com.pragma.powerup.domain.spi.IOrderTraceabilityPersistencePort;
import com.pragma.powerup.domain.spi.ISecurityContextPort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class OrderTraceabilityUseCase implements IOrderTraceabilityServicePort {

    private final IOrderTraceabilityPersistencePort persistencePort;
    private final ISecurityContextPort securityContextPort;

    public OrderTraceabilityUseCase(IOrderTraceabilityPersistencePort persistencePort, ISecurityContextPort securityContextPort) {
        this.persistencePort = persistencePort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public OrderTraceabilityModel createLog(OrderTraceabilityModel logModel) {
        if (logModel == null) {
            throw new RequiredFieldException("log");
        }
        if (logModel.getIdPedido() == null) {
            throw new RequiredFieldException("idPedido");
        }
        if (logModel.getIdCliente() == null) {
            throw new RequiredFieldException("idCliente");
        }
        if (logModel.getNuevoEstado() == null || logModel.getNuevoEstado().trim().isEmpty()) {
            throw new RequiredFieldException("nuevoEstado");
        }

        if (logModel.getFecha() == null) {
            logModel.setFecha(LocalDateTime.now());
        }

        return persistencePort.saveLog(logModel);
    }

    @Override
    public List<OrderTraceabilityModel> getLogsByOrderId(Long orderId) {
        if (orderId == null) {
            throw new RequiredFieldException("orderId");
        }

        List<OrderTraceabilityModel> logs = persistencePort.getLogsByOrderId(orderId);
        if (logs == null || logs.isEmpty()) {
            return Collections.emptyList();
        }

        Long currentUserId = securityContextPort.getCurrentUserId();
        if (currentUserId != null) {
            boolean isOwner = logs.stream()
                    .anyMatch(log -> log.getIdCliente() != null && log.getIdCliente().equals(currentUserId));
            if (!isOwner) {
                throw new TraceabilityOrderNotBelongToClientException();
            }
        }

        return logs;
    }

    @Override
    public List<OrderTraceabilityModel> getLogsForCurrentCustomer() {
        Long currentUserId = securityContextPort.getCurrentUserId();
        if (currentUserId == null) {
            throw new RequiredFieldException("cliente autenticado");
        }
        List<OrderTraceabilityModel> logs = persistencePort.getLogsByCustomerId(currentUserId);
        return logs != null ? logs : Collections.emptyList();
    }
}
