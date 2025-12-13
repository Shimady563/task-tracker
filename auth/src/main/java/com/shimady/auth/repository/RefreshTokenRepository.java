package com.shimady.auth.repository;

import com.shimady.auth.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private static final String PREFIX = "refresh-token-";

    @Value("${jwt.token.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String token, String email) {
        redisTemplate.opsForValue().set(PREFIX + email, token, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public String findByEmail(String email) {
        if (!redisTemplate.hasKey(PREFIX + email)) {
            throw new ResourceNotFoundException("Refresh token for user with email: " + email + " not found");
        }
        return redisTemplate.opsForValue().get(PREFIX + email);
    }

    public void deleteByEmail(String email) {
        redisTemplate.delete(PREFIX + email);
    }
}
