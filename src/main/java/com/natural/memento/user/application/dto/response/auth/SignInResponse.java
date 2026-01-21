package com.natural.memento.user.application.dto.response.auth;

import com.natural.memento.user.domain.entity.User;

public record SignInResponse(
        Long userId,
        String email,
        String nickName,
        String accessToken,
        String refreshToken
) {

    public static SignInResponse of(User user, String accessToken, String refreshToken) {
        return new SignInResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                accessToken,
                refreshToken
        );
    }
}