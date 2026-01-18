package com.natural.memento.user.domain.entity;

import com.natural.memento.interest.domain.entity.Interest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 512)
    private String password;

    @Embedded
    private UserProfile userProfile;

    @Embedded
    private Agree agree;

    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfilePicture> profilePictures = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInterest> userInterests = new ArrayList<>();

    private User(String email, String password, UserProfile userProfile, Agree agree) {
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
        this.agree = agree;
        this.authority = Authority.USER;
    }

    public static User create(
            String email,
            String password,
            UserProfile userProfile,
            Agree agree,
            Address address,
            List<ProfilePicture> profilePictures,
            List<Interest> interests
    ) {
        User user = new User(email, password, userProfile, agree);

        if (address != null) {
            user.addAddress(address);
        }

        if (profilePictures != null) {
            for (ProfilePicture picture : profilePictures) {
                user.addProfilePicture(picture);
            }
        }

        if (interests != null) {
            for (Interest interest : interests) {
                user.addInterest(interest);
            }
        }

        user.ensureDefaultPictureIfMissing();
        return user;
    }


    public void addAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("address는 null일 수 없습니다.");
        }

        address.assignTo(this);

        if (address.isDefault()) {
            this.addresses.forEach(a -> a.markDefault(false));
            address.markDefault(true);
        }

        this.addresses.add(address);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.assignTo(null);
    }

    public void addProfilePicture(ProfilePicture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture는 null일 수 없습니다.");
        }

        picture.assignTo(this);

        if (picture.getSortOrder() <= 0) {
            int next = this.profilePictures.stream()
                    .mapToInt(ProfilePicture::getSortOrder)
                    .max()
                    .orElse(0) + 1;
            picture.changeSortOrder(next);
        }

        if (picture.isDefault()) {
            this.profilePictures.forEach(p -> p.markDefault(false));
            picture.markDefault(true);
        }

        this.profilePictures.add(picture);
    }

    public void setDefaultPicture(Long pictureId) {
        if (pictureId == null) {
            throw new IllegalArgumentException("pictureId는 필수입니다.");
        }

        boolean found = false;
        for (ProfilePicture picture : this.profilePictures) {
            boolean isTarget = pictureId.equals(picture.getId());
            if (isTarget) {
                found = true;
            }
            picture.markDefault(isTarget);
        }
        if (!found) {
            throw new IllegalArgumentException("해당 프로필 사진이 없습니다.");
        }
    }

    public void reorderPictures(List<Long> orderedIds) {
        // TODO: 모든 id가 존재, 중복 없음, orderedIds.size == profilePictures.size

        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            ProfilePicture pic = this.profilePictures.stream()
                    .filter(p -> p.getId() != null && p.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("사진 id가 유효하지 않습니다: " + id));

            pic.changeSortOrder(i + 1);
        }
    }

    private void ensureDefaultPictureIfMissing() {
        if (this.profilePictures.isEmpty()) {
            return;
        }

        boolean hasDefault = this.profilePictures.stream().anyMatch(ProfilePicture::isDefault);
        if (!hasDefault) {
            ProfilePicture first = this.profilePictures.stream()
                    .min(Comparator.comparingInt(ProfilePicture::getSortOrder)
                            .thenComparingLong(a -> a.getId() == null ? Long.MAX_VALUE : a.getId()))
                    .orElseThrow();
            first.markDefault(true);
        }
    }

    public void addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("interest는 null일 수 없습니다.");
        }

        boolean exists = this.userInterests.stream()
                .anyMatch(u -> u.getInterest().getId().equals(interest.getId()));
        if (exists) {
            return;
        }

        UserInterest ui = UserInterest.create(this, interest);
        this.userInterests.add(ui);
    }
}
