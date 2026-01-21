package com.natural.memento.user.application.service;

import com.natural.memento.user.application.dto.response.user.GetUserResponse;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public GetUserResponse me(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_ID));

        return GetUserResponse.from(user);
    }


}
