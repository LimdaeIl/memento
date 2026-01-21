package com.natural.memento.user.application.service;

import com.natural.memento.commons.jwt.JwtErrorCode;
import com.natural.memento.commons.jwt.JwtTokenProvider;
import com.natural.memento.commons.jwt.TokenException;
import com.natural.memento.user.application.dto.request.SignInRequest;
import com.natural.memento.user.application.dto.request.SignupRequest;
import com.natural.memento.user.application.dto.response.SignInResponse;
import com.natural.memento.user.application.dto.response.SignupResponse;
import com.natural.memento.user.application.dto.response.TokenReissueResponse;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.exception.UserErrorCode;
import com.natural.memento.user.domain.exception.UserException;
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

        // 개발 편의를 위해 주석 처리
//        if (!userEmailAuthRepository.isVerified(request.email())) {
//            throw new AuthException(AuthErrorCode.EMAIL_AUTH_NOT_VERIFIED);
//        }

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

    @Transactional
    public TokenReissueResponse reissue(String at, String rt) {

        Long userId = jwtTokenProvider.getUserId(rt);

        if (tokenRepository.isRtBlacklisted(rt)) {
            throw new TokenException(JwtErrorCode.INVALID_REFRESH_TOKEN);
        }

        String storedRt = tokenRepository.findRt(userId);
        if (storedRt == null) {
            throw new TokenException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        if (!storedRt.equals(rt)) {
            throw new TokenException(JwtErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        long rtTtl = jwtTokenProvider.getRtTtlSeconds(rt);
        if (rtTtl > 0) {
            tokenRepository.blacklistRt(rt, rtTtl);
        }

        if (at != null && !at.isBlank()) {
            long atTtl = jwtTokenProvider.getAtTtlSeconds(at);
            if (atTtl > 0) {
                tokenRepository.blacklistAt(at, atTtl);
            }
        }

        String newAt = jwtTokenProvider.generateAt(userId, user.getEmail(), user.getRole());
        String newRt = jwtTokenProvider.generateRt(userId, user.getEmail(), user.getRole());
        long newRtTtl = jwtTokenProvider.getRtTtlSeconds(newRt);

        // 새 RT를 Redis에 저장 (사용 가능한 RT는 항상 '한 개'만 유지)
        tokenRepository.saveRefreshToken(userId, newRt, newRtTtl);

        return TokenReissueResponse.of(user.getId(), newAt, newRt);
    }

    @Transactional
    public void logout(String at, String rt) {
        Long userIdByRt = jwtTokenProvider.getUserId(rt);
        Long userIdByAt = jwtTokenProvider.getUserId(at);

        if (!userIdByRt.equals(userIdByAt)) {
            throw new TokenException(JwtErrorCode.INVALID_BEARER_TOKEN);
        }

        if (tokenRepository.isRtBlacklisted(rt)) {
            throw new TokenException(JwtErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (tokenRepository.isAtBlacklisted(at)) {
            throw new TokenException(JwtErrorCode.INVALID_ACCESS_TOKEN);
        }

        String storedRt = tokenRepository.findRt(userIdByRt);
        if (storedRt == null) {
            throw new TokenException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        if (!storedRt.equals(rt)) {
            throw new TokenException(JwtErrorCode.INVALID_REFRESH_TOKEN);
        }

        long rtTtl = jwtTokenProvider.getRtTtlSeconds(rt);
        if (rtTtl > 0) {
            tokenRepository.blacklistRt(rt, rtTtl);
        }

        long atTtl = jwtTokenProvider.getAtTtlSeconds(at);
        if (atTtl > 0) {
            tokenRepository.blacklistAt(at, atTtl);
        }

        tokenRepository.deleteRt(userIdByRt);

    }
}
