package com.mercadotech.authserver.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.mercadotech.authserver.logging.DefaultStructuredLogger;
import com.mercadotech.authserver.logging.StructuredLogger;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenCacheService implements TokenCacheService {

    private final StringRedisTemplate redisTemplate;
    private static final Duration TTL = Duration.ofHours(1);
    private final StructuredLogger logger = new DefaultStructuredLogger(RedisTokenCacheService.class);

    @Override
    public void save(String clientId, String token) {
        redisTemplate.opsForValue().set(clientId, token, TTL);
        logger.info(String.format("Cached token for client %s", clientId), null);
    }

    @Override
    public String get(String clientId) {
        String token = redisTemplate.opsForValue().get(clientId);
        if (token != null) {
            logger.info(String.format("Retrieved cached token for client %s", clientId), null);
        } else {
            logger.info(String.format("No cached token found for client %s", clientId), null);
        }
        return token;
    }
}
