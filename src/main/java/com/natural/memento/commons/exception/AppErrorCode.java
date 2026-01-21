package com.natural.memento.commons.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "공통: 잘못된 입력입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "공통: 서버 오류가 발생했습니다."),

    MAIL_SEND_FAILED(HttpStatus.BAD_GATEWAY, "공통: 이메일 전송에 실패했습니다."),
    MAIL_SENDER_UNAUTHORIZED(HttpStatus.BAD_GATEWAY, "공통: 발신자 주소가 허용되지 않습니다."),
    MAIL_AUTH_FAILED(HttpStatus.BAD_GATEWAY, "공통: 메일 서버 인증에 실패했습니다."),
    MAIL_CONNECT_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "공통: 메일 서버 연결에 실패했습니다.");


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
