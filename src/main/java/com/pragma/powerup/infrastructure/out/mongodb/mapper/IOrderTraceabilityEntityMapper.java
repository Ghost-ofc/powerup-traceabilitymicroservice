package com.pragma.powerup.infrastructure.out.mongodb.mapper;

import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import com.pragma.powerup.infrastructure.out.mongodb.entity.OrderTraceabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderTraceabilityEntityMapper {
    OrderTraceabilityEntity toEntity(OrderTraceabilityModel model);
    OrderTraceabilityModel toModel(OrderTraceabilityEntity entity);
    List<OrderTraceabilityModel> toModelList(List<OrderTraceabilityEntity> entityList);
}
