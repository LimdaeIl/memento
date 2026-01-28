package com.natural.memento.user.infrastructure.redis;

import com.natural.memento.user.domain.repository.SocialAuthExchangeRepository;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SocialAuthExchangeRedisRepository implements SocialAuthExchangeRepository {

    private final StringRedisTemplate redis;
    private static final String KEY = "auth:social:exchange:";

    @Override
    public String issue(Long userId, Duration ttl) {
        String code = UUID.randomUUID().toString().replace("-", "");
        redis.opsForValue().set(KEY + code, String.valueOf(userId), ttl);
        return code;
    }

    @Override
    public Long consume(String code) {
        String key = KEY + code;
        String v = redis.opsForValue().get(key);
        if (v == null) return null;
        redis.delete(key); // 1회 소모
        return Long.valueOf(v);
    }
}
