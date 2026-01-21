package com.natural.memento.user.application.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailCodeRequest(
        @Email(message = "이메일: 이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일: 이메일은 필수입니다.")
        String email
) {

}
