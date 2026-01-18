package com.natural.memento.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PUBLIC)
@Embeddable
public class Agree {

    @Column(name = "ToS_available", nullable = false)
    private boolean ToSAvailable;

    @Column(name = "PII_available", nullable = false)
    private boolean PIIAvailable;

    @Column(name = "Email_available", nullable = false)
    private boolean EmailAvailable;
}

