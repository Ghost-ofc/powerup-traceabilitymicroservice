package com.pragma.powerup.infrastructure.out.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "order_traceability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderTraceabilityEntity {

    @Id
    private String id;
    private Long idPedido;
    private Long idCliente;
    private String correoCliente;
    private LocalDateTime fecha;
    private String estadoAnterior;
    private String nuevoEstado;
    private Long idEmpleado;
    private String correoEmpleado;
}
