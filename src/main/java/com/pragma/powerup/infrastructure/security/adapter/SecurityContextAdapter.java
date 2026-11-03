package com.pragma.powerup.infrastructure.security.adapter;

import com.pragma.powerup.domain.spi.ISecurityContextPort;
import com.pragma.powerup.infrastructure.out.client.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.client.dto.UserClientDto;
import com.pragma.powerup.infrastructure.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityContextAdapter implements ISecurityContextPort {

    private final SecurityContextHelper securityContextHelper;
    private final IUserFeignClient userFeignClient;

    @Override
    public Long getCurrentUserId() {
        Long userId = securityContextHelper.getCurrentUserId();
        if (userId != null) {
            return userId;
        }
        String email = securityContextHelper.getCurrentUserEmail();
        if (email != null && !email.trim().isEmpty()) {
            try {
                UserClientDto user = userFeignClient.getUserByEmail(email);
                if (user != null) {
                    return user.getId();
                }
            } catch (Exception e) {
                log.warn("Could not retrieve user by email {} from user service", email, e);
            }
        }
        return null;
    }
}
