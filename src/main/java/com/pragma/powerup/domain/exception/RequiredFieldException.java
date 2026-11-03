package com.pragma.powerup.domain.exception;

public class RequiredFieldException extends DomainException {
    public RequiredFieldException(String field) {
        super(String.format("El campo '%s' es obligatorio.", field));
    }
}
