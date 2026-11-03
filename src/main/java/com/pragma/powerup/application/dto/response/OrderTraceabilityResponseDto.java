package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta con la información de un log de trazabilidad")
public class OrderTraceabilityResponseDto {

    @Schema(description = "ID del documento de trazabilidad", example = "654e38fb23a41b5907d81a9c")
    private String id;

    @Schema(description = "ID del pedido", example = "10")
    private Long idPedido;

    @Schema(description = "ID del cliente", example = "5")
    private Long idCliente;

    @Schema(description = "Correo del cliente", example = "cliente@correo.com")
    private String correoCliente;

    @Schema(description = "Fecha y hora del cambio de estado")
    private LocalDateTime fecha;

    @Schema(description = "Estado anterior del pedido", example = "PENDIENTE")
    private String estadoAnterior;

    @Schema(description = "Nuevo estado del pedido", example = "EN_PREPARACION")
    private String nuevoEstado;

    @Schema(description = "ID del empleado asignado o que realizó la acción", example = "20")
    private Long idEmpleado;

    @Schema(description = "Correo del empleado", example = "empleado@correo.com")
    private String correoEmpleado;
}
