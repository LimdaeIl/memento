package com.natural.memento.user.domain.exception;

import com.natural.memento.commons.exception.AppException;
import com.natural.memento.commons.exception.ErrorCode;

public class UserException extends AppException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }


}
