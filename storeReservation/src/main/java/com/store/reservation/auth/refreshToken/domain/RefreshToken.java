package com.store.reservation.auth.refreshToken.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "RefreshToken")
public class RefreshToken {

    @Id
    private String email;

    private final String refreshToken;
    @TimeToLive
    private final Long expiration;

    @Builder
    public RefreshToken(String email, String refreshToken, Long expiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
