package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de solicitud para registrar log de trazabilidad de un pedido")
public class OrderTraceabilityRequestDto {

    @NotNull(message = "El idPedido es obligatorio")
    @Schema(description = "ID del pedido", example = "10")
    private Long idPedido;

    @NotNull(message = "El idCliente es obligatorio")
    @Schema(description = "ID del cliente", example = "5")
    private Long idCliente;

    @Schema(description = "Correo del cliente", example = "cliente@correo.com")
    private String correoCliente;

    @Schema(description = "Fecha del cambio de estado (opcional, asignada automáticamente si es nula)")
    private LocalDateTime fecha;

    @Schema(description = "Estado anterior del pedido", example = "PENDIENTE")
    private String estadoAnterior;

    @NotBlank(message = "El nuevoEstado es obligatorio")
    @Schema(description = "Nuevo estado del pedido", example = "EN_PREPARACION")
    private String nuevoEstado;

    @Schema(description = "ID del empleado que realizó la acción", example = "20")
    private Long idEmpleado;

    @Schema(description = "Correo del empleado que realizó la acción", example = "empleado@correo.com")
    private String correoEmpleado;
}
