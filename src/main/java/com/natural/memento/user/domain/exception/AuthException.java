package com.natural.memento.user.domain.exception;

import com.natural.memento.commons.exception.AppException;
import com.natural.memento.commons.exception.ErrorCode;

public class AuthException extends AppException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
