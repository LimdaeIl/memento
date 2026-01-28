package com.natural.memento.user.application.service;

import com.natural.memento.commons.jwt.JwtTokenProvider;
import com.natural.memento.user.application.dto.response.auth.ExchangeResponse;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.exception.UserErrorCode;
import com.natural.memento.user.domain.exception.UserException;
import com.natural.memento.user.domain.repository.SocialAuthExchangeRepository;
import com.natural.memento.user.domain.repository.UserJpaRepository;
import com.natural.memento.user.domain.repository.UserTokenRepository;
import com.natural.memento.user.presentation.AuthController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialAuthService {

    private final SocialAuthExchangeRepository exchangeRepository;
    private final UserJpaRepository userJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserTokenRepository userTokenRepository;

    @Transactional
    public ExchangeResponse exchange(String code) {
        Long userId = exchangeRepository.consume(code);
        if (userId == null) {
            throw new AuthException(AuthErrorCode.SOCIAL_EXCHANGE_CODE_INVALID);
        }

        User user = userJpaRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String at = jwtTokenProvider.generateAt(user.getId(), user.getEmail(), user.getRole());
        String rt = jwtTokenProvider.generateRt(user.getId(), user.getEmail(), user.getRole());

        long rtTtl = jwtTokenProvider.getRtTtlSeconds(rt);
        userTokenRepository.saveRefreshToken(user.getId(), rt, rtTtl);

        return new ExchangeResponse(at, rt);
    }
}