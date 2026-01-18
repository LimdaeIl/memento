package com.natural.memento.user.domain.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum EducationLevel {
    HIGH_SCHOOL("고등학교"),
    ASSOCIATE_DEGREE("전문학사"),
    BACHELOR_DEGREE("학사"),
    MASTER_DEGREE("석사"),
    DOCTORATE("박사");

    private final String description;
}
