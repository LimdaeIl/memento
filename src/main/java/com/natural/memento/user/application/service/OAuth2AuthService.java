package com.natural.memento.user.application.service;

import com.natural.memento.user.domain.entity.SocialType;
import com.natural.memento.user.domain.entity.User;
import com.natural.memento.user.domain.exception.AuthErrorCode;
import com.natural.memento.user.domain.exception.AuthException;
import com.natural.memento.user.domain.repository.SocialAuthExchangeRepository;
import com.natural.memento.user.domain.repository.UserJpaRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuth2AuthService {

    private final UserJpaRepository userJpaRepository;
    private final SocialAuthExchangeRepository exchangeRepository;

    @Transactional
    public String handleGoogle(String email, String googleSub, String name, Duration ttl) {

        User user = userJpaRepository.findBySocialTypeAndSocialIdAndDeletedAtIsNull(
                        SocialType.GOOGLE, googleSub)
                .orElseGet(() -> {
                    // ⚠️ 여기서 "email이 LOCAL로 이미 존재" 정책 결정 필요
                    // 지금은 MVP: email 존재하면 그 유저에 GOOGLE 연동 or 예외
                    // 일단 안전한 방향: 존재하면 예외 추천
                    if (userJpaRepository.existsByEmailAndDeletedAtIsNull(email)) {
                        throw new AuthException(AuthErrorCode.SOCIAL_EMAIL_ALREADY_EXISTS);
                    }
                    User created = User.createSocial(email, name, SocialType.GOOGLE, googleSub);
                    return userJpaRepository.save(created);
                });

        return exchangeRepository.issue(user.getId(), ttl);
    }
}