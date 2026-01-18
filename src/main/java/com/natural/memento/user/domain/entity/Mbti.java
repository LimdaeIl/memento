package com.natural.memento.user.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum Mbti {

    ISTJ("ISTJ", "청렴결백한 논리주의자"),
    ISFJ("ISFJ", "용감한 수호자"),
    INFJ("INFJ", "선의의 옹호자"),
    INTJ("INTJ", "전략가"),

    ISTP("ISTP", "만능 재주꾼"),
    ISFP("ISFP", "호기심 많은 예술가"),
    INFP("INFP", "열정적인 중재자"),
    INTP("INTP", "논리적인 사색가"),

    ESTP("ESTP", "모험을 즐기는 사업가"),
    ESFP("ESFP", "자유로운 영혼의 연예인"),
    ENFP("ENFP", "재기발랄한 활동가"),
    ENTP("ENTP", "뜨거운 논쟁을 즐기는 변론가"),

    ESTJ("ESTJ", "엄격한 관리자"),
    ESFJ("ESFJ", "사교적인 외교관"),
    ENFJ("ENFJ", "정의로운 사회운동가"),
    ENTJ("ENTJ", "대담한 통솔자");

    private final String code;
    private final String description;
}
