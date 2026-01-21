package com.natural.memento.user.application.dto.response;

import com.natural.memento.user.domain.entity.User;

public record SignupResponse(
        Long id,
        String email,
        String nickname

) {

    public static SignupResponse from(User user) {
        return new SignupResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
