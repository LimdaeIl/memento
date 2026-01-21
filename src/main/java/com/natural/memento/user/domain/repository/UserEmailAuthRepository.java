package com.natural.memento.user.domain.repository;

import java.time.Duration;
import java.util.Optional;

public interface UserEmailAuthRepository {

    Optional<String> getCode(String email);

    void saveCode(String email, String code, Duration ttl);

    void deleteCode(String email);

    boolean hasCooldown(String email);

    void setCooldown(String email, Duration ttl);

    int incrementAttempts(String email, Duration ttlForAttemptsKey);

    void resetAttempts(String email);

    boolean isLocked(String email);

    void lock(String email, Duration lockTtl);

    void markVerified(String email, Duration ttl);

    boolean isVerified(String email);
}

