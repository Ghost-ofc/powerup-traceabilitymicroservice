package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderTraceabilityModel;

import java.util.List;

public interface IOrderTraceabilityPersistencePort {
    OrderTraceabilityModel saveLog(OrderTraceabilityModel logModel);
    List<OrderTraceabilityModel> getLogsByOrderId(Long orderId);
    List<OrderTraceabilityModel> getLogsByCustomerId(Long customerId);
}
