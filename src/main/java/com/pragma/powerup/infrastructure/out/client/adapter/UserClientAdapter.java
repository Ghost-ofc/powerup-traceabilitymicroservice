package com.pragma.powerup.infrastructure.out.client.adapter;

import com.pragma.powerup.domain.spi.IUserClientPort;
import com.pragma.powerup.infrastructure.out.client.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.client.dto.UserClientDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserClientAdapter implements IUserClientPort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean isUserOwner(Long userId) {
        if (userId == null) {
            return false;
        }
        try {
            UserClientDto user = userFeignClient.getUserById(userId);
            if (user == null || user.getRolNombre() == null) {
                return false;
            }
            String role = user.getRolNombre().trim();
            return role.equalsIgnoreCase("ROLE_PROPIETARIO") || role.equalsIgnoreCase("PROPIETARIO");
        } catch (FeignException e) {
            log.warn("Error calling user service for id {}: status {}", userId, e.status());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error checking user owner role for id {}", userId, e);
            return false;
        }
    }
}
