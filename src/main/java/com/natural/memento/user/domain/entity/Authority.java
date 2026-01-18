package com.natural.memento.user.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum Authority {
    USER("회원"),
    MANAGER("매니저"),
    ADMIN("관리자");

    private final String description;
}
