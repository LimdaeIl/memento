package com.natural.memento.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserAddress {

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "zipcode", length = 100)
    private String zipcode;
}

