package com.natural.memento.commons.jwt;

import com.natural.memento.commons.exception.AppException;
import com.natural.memento.commons.exception.ErrorCode;


public class TokenException extends AppException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(JwtErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
