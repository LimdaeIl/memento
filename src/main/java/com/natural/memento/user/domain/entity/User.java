package com.natural.memento.user.domain.entity;

import com.natural.memento.commons.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", length = 512)
    private String password;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "phone", length = 11) // nullable true
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 20)
    private UserRole role;

    @Embedded
    private UserAddress address;

    @Column(name = "profile_image", length = 300)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", length = 20)
    private SocialType socialType;

    @Column(name = "social_id", length = 255)
    private String socialId;


    private User(String email, String password, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.role = UserRole.USER;
    }

    public static User createSocial(String email, String nickname, SocialType type, String socialId) {
        User user = new User(email, null, nickname, null); // password/phone 없음
        user.socialType = type;
        user.socialId = socialId;
        return user;
    }

    public static User create(String email, String password, String nickname, String phone) {
        return new User(email, password, nickname, phone);
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateAddress(UserAddress address) {
        this.address = address;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void linkSocial(SocialType type, String socialId) {
        this.socialType = type;
        this.socialId = socialId;
    }

}


