package com.natural.memento.user.application.dto.response.sola;

public record VerifySMSResponse(
        boolean verified,
        String verificationToken
) {
    public static VerifySMSResponse success(String token) {
        return new VerifySMSResponse(true, token);
    }

    public static VerifySMSResponse fail() {
        return new VerifySMSResponse(false, null);
    }
}
