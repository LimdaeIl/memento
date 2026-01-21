package com.natural.memento.user.application.service;

import com.natural.memento.user.application.dto.request.SendEmailCodeRequest;
import com.natural.memento.user.application.dto.request.VerifyEmailCodeRequest;
import com.natural.memento.user.application.dto.response.SendEmailCodeResponse;
import com.natural.memento.user.application.dto.response.VerifyEmailCodeResponse;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.repository.UserEmailAuthRepository;
import com.natural.memento.user.domain.repository.UserJpaRepository;
import com.natural.memento.user.infrastructure.email.AuthEmailProperties;
import com.natural.memento.user.infrastructure.email.EmailSender;
import com.natural.memento.user.infrastructure.email.template.EmailTemplateRenderer;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "EmailService")
@RequiredArgsConstructor
@Service
public class EmailService {

    private final UserJpaRepository userJpaRepository;
    private final UserEmailAuthRepository userEmailAuthRepository;
    private final EmailTemplateRenderer templateRenderer;
    private final EmailSender emailSender;
    private final AuthEmailProperties props;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public SendEmailCodeResponse sendEmailCode(SendEmailCodeRequest request) {
        if (userJpaRepository.existsByEmail(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_EXISTS);
        }

        if (userEmailAuthRepository.isLocked(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_LOCKED);
        }

        if (userEmailAuthRepository.hasCooldown(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_COOLDOWN);
        }

        String code = generate6DigitCode();

        userEmailAuthRepository.saveCode(request.email(), code, props.codeValidityDuration());
        userEmailAuthRepository.setCooldown(request.email(), props.resendCooldownDuration());

        String subject = props.template() != null
                && props.template().subject() != null
                ? props.template().subject()
                : "[memento] 이메일 인증 코드";

        long minutes = Math.max(1, props.codeValidityDuration().toMinutes());
        String html = templateRenderer.render(
                "email/email-auth",
                Map.of(
                        "brand", "memento",
                        "purpose", "signup",
                        "code", code,
                        "minutes", minutes
                )
        );
        log.info("Rendered mail html length={}", html.length());

        emailSender.sendCode(request.email(), subject, html);

        return SendEmailCodeResponse.of(request.email());
    }

    public VerifyEmailCodeResponse verifyCode(VerifyEmailCodeRequest request) {

        if (userEmailAuthRepository.isLocked(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_LOCKED);
        }

        String saved = userEmailAuthRepository.getCode(request.email())
                .orElseThrow(() -> new AuthException(AuthErrorCode.EMAIL_AUTH_EXPIRED));

        if (!saved.equals(request.code())) {
            int attempts = userEmailAuthRepository.incrementAttempts(request.email(),
                    props.codeValidityDuration());

            if (attempts >= props.maxAttempts()) {
                userEmailAuthRepository.lock(request.email(),
                        props.lockoutDuration());
                throw new AuthException(AuthErrorCode.EMAIL_AUTH_LOCKED);
            }
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_CODE_MISMATCH);
        }

        userEmailAuthRepository.deleteCode(request.email());
        userEmailAuthRepository.resetAttempts(request.email());
        userEmailAuthRepository.markVerified(request.email(),
                props.successValidityDuration());

        return VerifyEmailCodeResponse.of(request.email());
    }

    private static String generate6DigitCode() {
        int n = RANDOM.nextInt(1_000_000); // 0 ~ 999999
        return String.format("%06d", n);
    }

    private static String toMinuteText(Duration duration) {
        long minutes = Math.max(1, duration.toMinutes());
        return minutes + "분";
    }
}
