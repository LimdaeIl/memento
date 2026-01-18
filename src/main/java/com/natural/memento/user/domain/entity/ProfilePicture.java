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
@Table(name = "v1_profile_pictures")
@Entity
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture_url", nullable = false, length = 512)
    private String pictureUrl;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private ProfilePicture(
            String pictureUrl,
            int sortOrder,
            boolean isDefault,
            User user) {
        this.pictureUrl = pictureUrl;
        this.sortOrder = sortOrder;
        this.isDefault = isDefault;
        this.user = user;
    }


    public static ProfilePicture create(
            String pictureUrl,
            int sortOrder,
            boolean isDefault,
            User user) {
        return new ProfilePicture(pictureUrl, sortOrder, isDefault, user);
    }

    public void assignTo(User user) {
        this.user = user;
    }

    public void markDefault(boolean value) {
        this.isDefault = value;
    }

    public void changeSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
