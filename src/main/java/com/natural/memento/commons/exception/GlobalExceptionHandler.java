package com.natural.memento.commons.exception;

import com.natural.memento.commons.response.ErrorResponse;
import com.natural.memento.commons.response.ErrorResponse.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String type = toProblemType(title);
        String instance = request.getRequestURI();
        String errorCode = title;

        return ResponseEntity.status(code.status())
                .body(ErrorResponse.of(
                        type,
                        title,
                        code.status(),
                        ex.getMessage(),
                        instance,
                        errorCode,
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
        String type = toProblemType(title);
        String instance = request.getRequestURI();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                        type,
                        title,
                        HttpStatus.BAD_REQUEST,
                        code.message(),
                        instance,
                        title,
                        errors
                ));
    }

    private static String toProblemType(String title) {
        return PROBLEM_BASE_URI;
    }

}
