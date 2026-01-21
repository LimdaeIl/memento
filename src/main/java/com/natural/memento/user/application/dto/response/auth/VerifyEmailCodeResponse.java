package com.natural.memento.user.application.dto.response.auth;

public record VerifyEmailCodeResponse(
        String email,
        boolean verified
) {

    public static VerifyEmailCodeResponse of(String email) {
        return new VerifyEmailCodeResponse(email, true);
    }
}
