package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderTraceabilityRequestDto;
import com.pragma.powerup.application.dto.response.OrderTraceabilityResponseDto;
import com.pragma.powerup.application.handler.IOrderTraceabilityHandler;
import com.pragma.powerup.application.mapper.IOrderTraceabilityRequestMapper;
import com.pragma.powerup.application.mapper.IOrderTraceabilityResponseMapper;
import com.pragma.powerup.domain.api.IOrderTraceabilityServicePort;
import com.pragma.powerup.domain.model.OrderTraceabilityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTraceabilityHandler implements IOrderTraceabilityHandler {

    private final IOrderTraceabilityServicePort servicePort;
    private final IOrderTraceabilityRequestMapper requestMapper;
    private final IOrderTraceabilityResponseMapper responseMapper;

    @Override
    public OrderTraceabilityResponseDto createLog(OrderTraceabilityRequestDto requestDto) {
        OrderTraceabilityModel model = requestMapper.toModel(requestDto);
        OrderTraceabilityModel savedModel = servicePort.createLog(model);
        return responseMapper.toResponse(savedModel);
    }

    @Override
    public List<OrderTraceabilityResponseDto> getLogsByOrderId(Long orderId) {
        List<OrderTraceabilityModel> logs = servicePort.getLogsByOrderId(orderId);
        return responseMapper.toResponseList(logs);
    }

    @Override
    public List<OrderTraceabilityResponseDto> getLogsForCurrentCustomer() {
        List<OrderTraceabilityModel> logs = servicePort.getLogsForCurrentCustomer();
        return responseMapper.toResponseList(logs);
    }
}
