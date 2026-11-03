package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.entity.OrderTraceabilityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IOrderTraceabilityMongoRepository extends MongoRepository<OrderTraceabilityEntity, String> {
    List<OrderTraceabilityEntity> findByIdPedidoOrderByFechaAsc(Long idPedido);
    List<OrderTraceabilityEntity> findByIdClienteOrderByFechaDesc(Long idCliente);
}
