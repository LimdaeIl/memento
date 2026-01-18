package com.natural.memento.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_addresses")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "zip", nullable = false, length = 100)
    private String zip;

    @Column(name = "details", length = 200)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    private Address(
            String city,
            String street,
            String zip,
            String details,
            User user,
            boolean isDefault) {
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.details = details;
        this.user = user;
        this.isDefault = isDefault;
    }

    public static Address create(
            String city,
            String street,
            String zip,
            String details,
            User user,
            boolean isDefault) {
        return new Address(city, street, zip, details, user, isDefault);
    }

    public void assignTo(User user) {
        this.user = user;
    }


    public void markDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
