package com.shimady.auth.repository;

import com.shimady.auth.TestcontainersConfiguration;
import com.shimady.auth.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers(disabledWithoutDocker = true)
public class RefreshTokenRepositoryTest {
    private static final String PREFIX = "refresh-token-";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void testSave() {
        var newToken = "newToken";
        var email = "newEmail";

        refreshTokenRepository.save(newToken, email);

        String foundToken = redisTemplate.opsForValue().get(PREFIX + email);

        assertNotNull(foundToken);
        assertEquals(newToken, foundToken);
    }

    @Test
    public void testFindByEmailFailure() {
        assertThrows(ResourceNotFoundException.class,
                () -> refreshTokenRepository.findByEmail("non existing"));
    }

    @Test
    void testDeleteByEmail() {
        var token = "token";
        var email = "email";

        redisTemplate.opsForValue().set(PREFIX + email, token, 1, TimeUnit.DAYS);

        refreshTokenRepository.deleteByEmail(email);

        assertThrows(ResourceNotFoundException.class,
                () -> refreshTokenRepository.findByEmail(token));
    }
}
