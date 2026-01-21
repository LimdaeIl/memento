package com.natural.memento.interest.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Table(name = "v1_interests")
@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "interest_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InterestType interestType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    private Interest(InterestType interestType, String name) {
        this.interestType = interestType;
        this.name = name;
    }

    public static Interest create(InterestType interestType, String name) {
        return new Interest(interestType, name);
    }


}
