package com.natural.memento.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.ZonedDateTime;
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
public class UserProfile {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "introduction", nullable = true, length = 300)
    private String introduction;

    @Column(name = "mbti", nullable = false, length = 4)
    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Column(name = "gender", nullable = false, length = 4)
    private String gender;

    @Column(name = "birthday", nullable = false, length = 4)
    private ZonedDateTime birthday;

    @Embedded
    private Education education;

}
