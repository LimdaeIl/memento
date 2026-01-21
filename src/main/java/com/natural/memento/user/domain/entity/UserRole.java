package com.natural.memento.user.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    ROLE_USER("일반회원"),
    ROLE_ADMIN("관리자"),
    ROLE_MANAGER("매니저");

    private final String description;

}
