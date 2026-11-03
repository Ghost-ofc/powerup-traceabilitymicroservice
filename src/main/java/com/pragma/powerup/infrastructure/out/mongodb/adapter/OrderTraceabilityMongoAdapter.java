package com.pragma.powerup.infrastructure.out.mongodb.adapter;

import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import com.pragma.powerup.domain.spi.IOrderTraceabilityPersistencePort;
import com.pragma.powerup.infrastructure.out.mongodb.entity.OrderTraceabilityEntity;
import com.pragma.powerup.infrastructure.out.mongodb.mapper.IOrderTraceabilityEntityMapper;
import com.pragma.powerup.infrastructure.out.mongodb.repository.IOrderTraceabilityMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderTraceabilityMongoAdapter implements IOrderTraceabilityPersistencePort {

    private final IOrderTraceabilityMongoRepository mongoRepository;
    private final IOrderTraceabilityEntityMapper entityMapper;

    @Override
    public OrderTraceabilityModel saveLog(OrderTraceabilityModel logModel) {
        OrderTraceabilityEntity entity = entityMapper.toEntity(logModel);
        OrderTraceabilityEntity savedEntity = mongoRepository.save(entity);
        return entityMapper.toModel(savedEntity);
    }

    @Override
    public List<OrderTraceabilityModel> getLogsByOrderId(Long orderId) {
        List<OrderTraceabilityEntity> entities = mongoRepository.findByIdPedidoOrderByFechaAsc(orderId);
        return entityMapper.toModelList(entities);
    }

    @Override
    public List<OrderTraceabilityModel> getLogsByCustomerId(Long customerId) {
        List<OrderTraceabilityEntity> entities = mongoRepository.findByIdClienteOrderByFechaDesc(customerId);
        return entityMapper.toModelList(entities);
    }
}
