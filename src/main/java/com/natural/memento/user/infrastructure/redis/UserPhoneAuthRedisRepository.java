package com.natural.memento.user.infrastructure.redis;

import com.natural.memento.user.domain.type.PhoneAuthPurpose;
import com.natural.memento.user.domain.repository.UserPhoneAuthRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserPhoneAuthRedisRepository implements UserPhoneAuthRepository {

    private final StringRedisTemplate redis;

    private static final String CODE = "auth:phone:code:";
    private static final String COOLDOWN = "auth:phone:cooldown:";
    private static final String ATTEMPTS = "auth:phone:attempts:";
    private static final String LOCKOUT = "auth:phone:lockout:";

    // 토큰 저장소
    private static final String TOKEN = "auth:phone:token:";

    @Override
    public Optional<String> getCode(PhoneAuthPurpose purpose, String phone) {
        return Optional.ofNullable(redis.opsForValue().get(codeKey(purpose, phone)));
    }

    @Override
    public void saveCode(PhoneAuthPurpose purpose, String phone, String code, Duration ttl) {
        redis.opsForValue().set(codeKey(purpose, phone), code, ttl);
    }

    @Override
    public void deleteCode(PhoneAuthPurpose purpose, String phone) {
        redis.delete(codeKey(purpose, phone));
    }

    @Override
    public boolean hasCooldown(PhoneAuthPurpose purpose, String phone) {
        return Boolean.TRUE.equals(redis.hasKey(cooldownKey(purpose, phone)));
    }

    @Override
    public void setCooldown(PhoneAuthPurpose purpose, String phone, Duration ttl) {
        redis.opsForValue().set(cooldownKey(purpose, phone), "1", ttl);
    }

    @Override
    public int incrementAttempts(PhoneAuthPurpose purpose, String phone,
            Duration ttlForAttemptsKey) {
        String key = attemptsKey(purpose, phone);
        Long value = redis.opsForValue().increment(key);
        redis.expire(key, ttlForAttemptsKey);
        return value == null ? 0 : value.intValue();
    }

    @Override
    public void resetAttempts(PhoneAuthPurpose purpose, String phone) {
        redis.delete(attemptsKey(purpose, phone));
    }

    @Override
    public boolean isLocked(PhoneAuthPurpose purpose, String phone) {
        return Boolean.TRUE.equals(redis.hasKey(lockoutKey(purpose, phone)));
    }

    @Override
    public void lock(PhoneAuthPurpose purpose, String phone, Duration lockTtl) {
        redis.opsForValue().set(lockoutKey(purpose, phone), "1", lockTtl);
    }

    @Override
    public String issueVerificationToken(PhoneAuthPurpose purpose, String phone, Duration ttl) {
        String token = UUID.randomUUID().toString().replace("-", "");
        // token -> phone 바인딩 (purpose별로 공간 분리)
        redis.opsForValue().set(tokenKey(purpose, token), phone, ttl);
        return token;
    }

    @Override
    public boolean consumeVerificationToken(PhoneAuthPurpose purpose, String phone, String token) {
        String key = tokenKey(purpose, token);
        String savedPhone = redis.opsForValue().get(key);
        if (savedPhone == null) {
            return false;
        }
        if (!savedPhone.equals(phone)) {
            return false;
        }
        redis.delete(key);
        return true;
    }

    private String codeKey(PhoneAuthPurpose purpose, String phone) {
        return CODE + purpose.name() + ":" + phone;
    }

    private String cooldownKey(PhoneAuthPurpose purpose, String phone) {
        return COOLDOWN + purpose.name() + ":" + phone;
    }

    private String attemptsKey(PhoneAuthPurpose purpose, String phone) {
        return ATTEMPTS + purpose.name() + ":" + phone;
    }

    private String lockoutKey(PhoneAuthPurpose purpose, String phone) {
        return LOCKOUT + purpose.name() + ":" + phone;
    }

    private String tokenKey(PhoneAuthPurpose purpose, String token) {
        return TOKEN + purpose.name() + ":" + token;
    }
}

