package com.natural.memento.user.application.dto.request.auth;

import jakarta.validation.constraints.NotNull;

public record TokenReissueRequest(
        @NotNull(message = "리프레시 토큰: 리프레시 토큰은 필수입니다.")
        String refreshToken
) {

}
