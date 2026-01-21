package com.natural.memento.user.infrastructure.redis;

import com.natural.memento.user.domain.repository.UserEmailAuthRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserEmailAuthRedisRepository implements UserEmailAuthRepository {

    private final StringRedisTemplate redis;

    private static final String CODE = "auth:email:code:";
    private static final String COOLDOWN = "auth:email:cooldown:";
    private static final String ATTEMPTS = "auth:email:attempts:";
    private static final String LOCKOUT = "auth:email:lockout:";
    private static final String VERIFIED = "auth:email:verified:";


    @Override
    public Optional<String> getCode(String email) {
        String code = redis.opsForValue().get(CODE + email);

        return Optional.ofNullable(code);
    }

    @Override
    public void saveCode(String email, String code, Duration ttl) {
        redis.opsForValue().set(CODE + email, code, ttl);
    }

    @Override
    public void deleteCode(String email) {
        redis.delete(CODE + email);
    }

    @Override
    public boolean hasCooldown(String email) {
        return Boolean.TRUE.equals(redis.hasKey(COOLDOWN + email));
    }

    @Override
    public void setCooldown(String email, Duration ttl) {
        redis.opsForValue().set(COOLDOWN + email, "1", ttl);
    }

    @Override
    public int incrementAttempts(String email, Duration ttlForAttemptsKey) {
        String key = ATTEMPTS + email;
        Long value = redis.opsForValue().increment(key);
        redis.expire(key, ttlForAttemptsKey);

        return value == null ? 0 : value.intValue();
    }

    @Override
    public void resetAttempts(String email) {
        redis.delete(ATTEMPTS + email);
    }

    @Override
    public boolean isLocked(String email) {
        return Boolean.TRUE.equals(redis.hasKey(LOCKOUT + email));
    }

    @Override
    public void lock(String email, Duration lockTtl) {
        redis.opsForValue().set(LOCKOUT + email, "1", lockTtl);
    }

    @Override
    public void markVerified(String email, Duration ttl) {
        redis.opsForValue().set(VERIFIED + email, "1", ttl);
    }

    @Override
    public boolean isVerified(String email) {
        return Boolean.TRUE.equals(redis.hasKey(VERIFIED + email));
    }
}

