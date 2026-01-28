package com.natural.memento.user.application.service;

import com.natural.memento.user.domain.type.PhoneAuthPurpose;
import com.natural.memento.user.domain.repository.UserPhoneAuthRepository;
import com.natural.memento.user.infrastructure.solapi.SmsSender;
import java.security.SecureRandom;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PhoneVerificationService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration COOLDOWN_TTL = Duration.ofSeconds(5);
    private static final Duration LOCKOUT_TTL = Duration.ofMinutes(10);

    // 토큰 TTL: signup 폼 작성 시간 고려해서 10분 정도 추천
    private static final Duration VERIFICATION_TOKEN_TTL = Duration.ofMinutes(10);

    private static final int MAX_ATTEMPTS = 5;

    private final UserPhoneAuthRepository userPhoneAuthRepository;
    private final SmsSender smsSender;

    @Transactional
    public void sendSMS(PhoneAuthPurpose purpose, String rawPhoneNumber) {
        String phone = normalize(rawPhoneNumber);

        if (userPhoneAuthRepository.isLocked(purpose, phone)) {
            throw new IllegalStateException("휴대폰 인증이 잠금 상태입니다.");
        }

        if (userPhoneAuthRepository.hasCooldown(purpose, phone)) {
            throw new IllegalStateException("인증번호 재전송 쿨다운 중입니다.");
        }

        String code = generate6DigitCode();

        userPhoneAuthRepository.saveCode(purpose, phone, code, CODE_TTL);
        userPhoneAuthRepository.setCooldown(purpose, phone, COOLDOWN_TTL);

        smsSender.send(phone, "[memento] 인증번호는 " + code + " 입니다. (5분 내 입력)");
    }

    /**
     * @return verificationToken (성공 시), 실패 시 null
     */
    @Transactional
    public String verifySMSAndIssueToken(PhoneAuthPurpose purpose, String rawPhoneNumber,
            String inputCode) {
        String phone = normalize(rawPhoneNumber);

        if (userPhoneAuthRepository.isLocked(purpose, phone)) {
            throw new IllegalStateException("휴대폰 인증이 잠금 상태입니다.");
        }

        String saved = userPhoneAuthRepository.getCode(purpose, phone).orElse(null);
        if (saved == null) {
            return null;
        }

        if (!saved.equals(inputCode)) {
            int attempts = userPhoneAuthRepository.incrementAttempts(purpose, phone, CODE_TTL);
            if (attempts >= MAX_ATTEMPTS) {
                userPhoneAuthRepository.lock(purpose, phone, LOCKOUT_TTL);
                throw new IllegalStateException("인증 시도 횟수를 초과했습니다.");
            }
            return null;
        }

        userPhoneAuthRepository.deleteCode(purpose, phone);
        userPhoneAuthRepository.resetAttempts(purpose, phone);

        return userPhoneAuthRepository.issueVerificationToken(purpose, phone,
                VERIFICATION_TOKEN_TTL);
    }

    @Transactional
    public boolean consumeVerificationToken(PhoneAuthPurpose purpose, String rawPhoneNumber,
            String token) {
        String phone = normalize(rawPhoneNumber);
        return userPhoneAuthRepository.consumeVerificationToken(purpose, phone, token);
    }

    private static String generate6DigitCode() {
        int num = RANDOM.nextInt(1_000_000);
        return String.format("%06d", num);
    }

    private static String normalize(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }
}
