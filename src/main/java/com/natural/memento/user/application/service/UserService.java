package com.natural.memento.user.application.service;

import com.natural.memento.user.application.dto.request.user.UpdateUserRequest;
import com.natural.memento.user.application.dto.response.user.GetUserResponse;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.entity.UserAddress;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.repository.UserEmailAuthRepository;
import com.natural.memento.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserEmailAuthRepository userEmailAuthRepository;

    @Transactional(readOnly = true)
    public GetUserResponse me(Long userId) {
        User user = userJpaRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_ID));

        return GetUserResponse.from(user);
    }

    @Transactional
    public GetUserResponse updateEmail(Long userId, String email) {

        if (!userEmailAuthRepository.isVerified(email)) {
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_NOT_VERIFIED);
        }

        User user = userJpaRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_ID));

        user.updateEmail(email);

        return GetUserResponse.from(user);
    }

    @Transactional
    public GetUserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userJpaRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_ID));

        // nickname
        if (hasText(request.nickname())) {
            user.updateNickname(request.nickname().trim());
        }

        // profileImage
        if (hasText(request.profileImage())) {
            user.updateProfileImage(request.profileImage().trim());
        }

        UserAddress curr = user.getAddress();

        String city = hasText(request.city()) ? request.city().trim()
                : (curr != null ? curr.getCity() : null);
        String street = hasText(request.street()) ? request.street().trim()
                : (curr != null ? curr.getStreet() : null);
        String zipcode = hasText(request.zipcode()) ? request.zipcode().trim()
                : (curr != null ? curr.getZipcode() : null);

        user.updateAddress(UserAddress.of(city, street, zipcode));

        return GetUserResponse.from(user);
    }

    private boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    @Transactional
    public void delete(Long userId) {
        User user = userJpaRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_ID));

        user.softDelete(user.getId());
    }
}
