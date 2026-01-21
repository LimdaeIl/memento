package com.natural.memento.user.domain.exception;

import com.natural.memento.commons.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "인증/인가 : 가입된 회원이 아닙니다. 회원 ID: %s"),

    EMAIL_EXISTS(HttpStatus.CONFLICT, "인증/인가: 이미 존재하는 이메일입니다."),
    EMAIL_AUTH_LOCKED(HttpStatus.FORBIDDEN, "인증/인가: 이메일 인증 시도 횟수를 초과하여 잠시 차단되었습니다."),
    EMAIL_AUTH_COOLDOWN(HttpStatus.TOO_MANY_REQUESTS, "인증/인가: 이메일 인증 코드는 잠시 후 다시 요청할 수 있습니다."),
    EMAIL_AUTH_EXPIRED(HttpStatus.BAD_REQUEST, "인증/인가: 이메일 인증 코드가 만료되었거나 존재하지 않습니다."),
    EMAIL_AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증/인가: 이메일 인증 코드가 일치하지 않습니다."),
    EMAIL_AUTH_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "인증/인가: 인증되지 않은 이메일입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}
