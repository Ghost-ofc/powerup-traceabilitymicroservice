package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderTraceabilityModel;

import java.util.List;

public interface IOrderTraceabilityServicePort {
    OrderTraceabilityModel createLog(OrderTraceabilityModel logModel);
    List<OrderTraceabilityModel> getLogsByOrderId(Long orderId);
    List<OrderTraceabilityModel> getLogsForCurrentCustomer();
}
