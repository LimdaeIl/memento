package com.natural.memento.user.application.dto.request.user;

import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(
        @Pattern(
                regexp = "^[A-Za-z0-9가-힣!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./]{1,12}$",
                message = "닉네임: 1~12자, 영문/숫자/한글/특수기호만 허용합니다."
        )
        String nickname,
        String city,
        String street,
        String zipcode,
        String profileImage
) {

}
