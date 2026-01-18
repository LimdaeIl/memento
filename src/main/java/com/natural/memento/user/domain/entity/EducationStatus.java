package com.natural.memento.user.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum EducationStatus {
    ENROLLED("재학"),
    LEAVE_OF_ABSENCE("휴학"),
    DROPPED_OUT("중퇴"),
    GRADUATED("졸업");

    private final String description;
}
