package com.natural.memento.user.application.service;

import com.natural.memento.commons.jwt.JwtTokenProvider;
import com.natural.memento.user.application.dto.request.SignInRequest;
import com.natural.memento.user.application.dto.request.SignupRequest;
import com.natural.memento.user.application.dto.response.SignInResponse;
import com.natural.memento.user.application.dto.response.SignupResponse;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (!userEmailAuthRepository.isVerified(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_AUTH_NOT_VERIFIED);
        }

        if (userJpaRepository.existsByEmail(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_EXISTS);
        }

        User user = User.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname()
        );

        userJpaRepository.save(user);

        return SignupResponse.from(user);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        User user = userJpaRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_BY_EMAIL,
                        request.email()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.USER_PASSWORD_INCORRECT);
        }

        String at = jwtTokenProvider.generateAt(user.getId(), user.getEmail(), user.getRole());
        String rt = jwtTokenProvider.generateRt(user.getId(), user.getEmail(), user.getRole());

        long refreshTtlMs = jwtTokenProvider.getRtTtlSeconds(rt);

        tokenRepository.saveRefreshToken(user.getId(), rt, refreshTtlMs);

        return SignInResponse.of(user, at, rt);
    }
}
