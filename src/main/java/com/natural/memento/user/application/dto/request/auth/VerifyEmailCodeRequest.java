package com.natural.memento.user.application.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailCodeRequest(
        @Email(message = "이메일: 이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일: 이메일은 필수입니다.")
        String email,


        @NotBlank(message = "이메일: 이메일 인증 코드는 필수입니다.")
        @Pattern(regexp = "^[0-9]{6}$", message = "인증 코드는 6자리 숫자여야 합니다.")
        String code
) {

}
