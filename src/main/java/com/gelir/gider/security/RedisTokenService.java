package com.gelir.gider.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String TOKEN_PREFIX = "auth:token:";

    public void storeToken(String token, Long userId, String roles, Duration ttl) {
        String key = TOKEN_PREFIX + token;
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("userId", userId);
        tokenData.put("roles", roles);

        redisTemplate.opsForHash().putAll(key, tokenData);
        redisTemplate.expire(key, ttl);
    }

    public boolean validateToken(String token) {
        String key = TOKEN_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Long getUserIdFromToken(String token) {
        String key = TOKEN_PREFIX + token;
        Object userId = redisTemplate.opsForHash().get(key, "userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    public String getRolesFromToken(String token) {
        String key = TOKEN_PREFIX + token;
        Object roles = redisTemplate.opsForHash().get(key, "roles");
        return roles != null ? roles.toString() : null;
    }

    public void deleteToken(String token) {
        String key = TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }

    public void refreshTokenExpiry(String token, Duration ttl) {
        String key = TOKEN_PREFIX + token;
        redisTemplate.expire(key, ttl);
    }
}
