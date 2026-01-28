package com.natural.memento.user.domain.repository;

import com.natural.memento.user.domain.type.PhoneAuthPurpose;
import java.time.Duration;
import java.util.Optional;


public interface UserPhoneAuthRepository {

    Optional<String> getCode(PhoneAuthPurpose purpose, String phone);

    void saveCode(PhoneAuthPurpose purpose, String phone, String code, Duration ttl);

    void deleteCode(PhoneAuthPurpose purpose, String phone);

    boolean hasCooldown(PhoneAuthPurpose purpose, String phone);

    void setCooldown(PhoneAuthPurpose purpose, String phone, Duration ttl);

    int incrementAttempts(PhoneAuthPurpose purpose, String phone, Duration ttlForAttemptsKey);

    void resetAttempts(PhoneAuthPurpose purpose, String phone);

    boolean isLocked(PhoneAuthPurpose purpose, String phone);

    void lock(PhoneAuthPurpose purpose, String phone, Duration lockTtl);

    String issueVerificationToken(PhoneAuthPurpose purpose, String phone, Duration ttl);

    boolean consumeVerificationToken(PhoneAuthPurpose purpose, String phone, String token);
}