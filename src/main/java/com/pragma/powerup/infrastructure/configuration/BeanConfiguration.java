package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IOrderTraceabilityServicePort;
import com.pragma.powerup.domain.spi.IOrderTraceabilityPersistencePort;
import com.pragma.powerup.domain.spi.ISecurityContextPort;
import com.pragma.powerup.domain.usecase.OrderTraceabilityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOrderTraceabilityPersistencePort persistencePort;
    private final ISecurityContextPort securityContextPort;

    @Bean
    public IOrderTraceabilityServicePort orderTraceabilityServicePort() {
        return new OrderTraceabilityUseCase(persistencePort, securityContextPort);
    }
}
