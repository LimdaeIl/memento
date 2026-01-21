package com.natural.memento.user.application.dto.response;

public record SendEmailCodeResponse(
        String email,
        boolean sent
) {

    public static SendEmailCodeResponse of(String email) {
        return new SendEmailCodeResponse(email, true);
    }
}
