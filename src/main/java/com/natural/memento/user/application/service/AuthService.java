package com.natural.memento.user.application.service;

import com.natural.memento.user.application.dto.request.SignupRequest;
import com.natural.memento.user.application.dto.response.SignupResponse;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.repository.UserEmailAuthRepository;
import com.natural.memento.user.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final UserEmailAuthRepository userEmailAuthRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (!userEmailAuthRepository.isVerified(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_NOT_VERIFIED);
        }

        User user = User.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname()
        );

        userJpaRepository.save(user);

        return SignupResponse.from(user);
    }
}
