package com.natural.memento.interest.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterestType {
    HOBBY("취미"),
    SPORT("스포츠"),
    PERSONALITY("성격"),
    MUSIC("음악"),
    BOOK("책"),
    MOVIE("영화"),
    FOOD("음식"),
    SEASON("계절");

    private final String description;
}
