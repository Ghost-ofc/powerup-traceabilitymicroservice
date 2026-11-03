package com.pragma.powerup.domain.spi;

public interface IUserClientPort {
    boolean isUserOwner(Long userId);
}
