package com.natural.memento.commons.exception;

import com.natural.memento.commons.response.ErrorResponse;
import com.natural.memento.commons.response.ErrorResponse.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_BASE_URI = "about:blank";

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleApp(AppException ex, HttpServletRequest request) {
        ErrorCode code = ex.getErrorCode();

        String title = ((Enum<?>) code).name();
        String instance = request.getRequestURI();

        return ResponseEntity.status(code.status())
                .body(ErrorResponse.of(
                        PROBLEM_BASE_URI,
                        title,
                        code.status(),
                        ex.getMessage(),
                        instance,
                        title,
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalid(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> FieldError.of(fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        AppErrorCode code = AppErrorCode.INVALID_INPUT_VALUE;
        String title = code.name();
        String instance = request.getRequestURI();

        return ResponseEntity.status(code.status())
                .body(ErrorResponse.of(
                        PROBLEM_BASE_URI,
                        title,
                        code.status(),
                        code.message(),
                        instance,
                        title,
                        errors
                ));
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleMail(MailException ex, HttpServletRequest request) {
        log.warn("Mail send failed. uri={}, message={}", request.getRequestURI(), ex.getMessage(),
                ex);

        AppErrorCode code = AppErrorCode.MAIL_SEND_FAILED;
        String title = code.name();

        return ResponseEntity.status(code.status())
                .body(ErrorResponse.of(
                        PROBLEM_BASE_URI,
                        title,
                        code.status(),
                        code.message(),
                        request.getRequestURI(),
                        title,
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception. uri={}, message={}", request.getRequestURI(),
                ex.getMessage(), ex);

        AppErrorCode code = AppErrorCode.INTERNAL_SERVER_ERROR;
        String title = code.name();

        return ResponseEntity.status(code.status())
                .body(ErrorResponse.of(
                        PROBLEM_BASE_URI,
                        title,
                        code.status(),
                        code.message(),
                        request.getRequestURI(),
                        title,
                        null
                ));
    }
}
