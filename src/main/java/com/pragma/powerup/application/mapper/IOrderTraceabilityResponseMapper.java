package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.OrderTraceabilityResponseDto;
import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderTraceabilityResponseMapper {
    OrderTraceabilityResponseDto toResponse(OrderTraceabilityModel model);
    List<OrderTraceabilityResponseDto> toResponseList(List<OrderTraceabilityModel> modelList);
}
