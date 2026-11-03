package com.pragma.powerup.domain.exception;

public class TraceabilityOrderNotBelongToClientException extends DomainException {
    public TraceabilityOrderNotBelongToClientException() {
        super("Solo puedes consultar el historial de logs de tus propios pedidos.");
    }
}
