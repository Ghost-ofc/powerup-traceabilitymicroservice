package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderTraceabilityRequestDto;
import com.pragma.powerup.application.dto.response.OrderTraceabilityResponseDto;

import java.util.List;

public interface IOrderTraceabilityHandler {
    OrderTraceabilityResponseDto createLog(OrderTraceabilityRequestDto requestDto);
    List<OrderTraceabilityResponseDto> getLogsByOrderId(Long orderId);
    List<OrderTraceabilityResponseDto> getLogsForCurrentCustomer();
}
