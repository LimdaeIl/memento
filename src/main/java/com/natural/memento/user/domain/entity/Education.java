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
public class Education {

    @Column(name = "education_level", nullable = false, length = 50)
    private EducationLevel level;

    @Column(name = "education_status", nullable = false, length = 50)
    private EducationStatus status;

    @Column(name = "major", nullable = false, length = 100)
    private String major;

    @Column(name = "school_name", nullable = false, length = 100)
    private String schoolName;
}
