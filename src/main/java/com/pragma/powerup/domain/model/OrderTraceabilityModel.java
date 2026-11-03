package com.pragma.powerup.domain.model;

import java.time.LocalDateTime;

public class OrderTraceabilityModel {
    private String id;
    private Long idPedido;
    private Long idCliente;
    private String correoCliente;
    private LocalDateTime fecha;
    private String estadoAnterior;
    private String nuevoEstado;
    private Long idEmpleado;
    private String correoEmpleado;

    public OrderTraceabilityModel() {
    }

    public OrderTraceabilityModel(String id, Long idPedido, Long idCliente, String correoCliente,
                                  LocalDateTime fecha, String estadoAnterior, String nuevoEstado,
                                  Long idEmpleado, String correoEmpleado) {
        this.id = id;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.correoCliente = correoCliente;
        this.fecha = fecha;
        this.estadoAnterior = estadoAnterior;
        this.nuevoEstado = nuevoEstado;
        this.idEmpleado = idEmpleado;
        this.correoEmpleado = correoEmpleado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCorreoEmpleado() {
        return correoEmpleado;
    }

    public void setCorreoEmpleado(String correoEmpleado) {
        this.correoEmpleado = correoEmpleado;
    }
}
