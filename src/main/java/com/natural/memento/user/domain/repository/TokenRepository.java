package com.natural.memento.user.domain.repository;

public interface TokenRepository {

    void saveRefreshToken(Long userId, String refreshToken, long ttlMillis);

    String findRt(Long userId);

    void deleteRefreshToken(Long userId);

    void blacklistAt(String accessToken, long ttlMillis);

    void blacklistRefreshToken(String refreshToken, long ttlMillis);

    boolean isAccessTokenBlacklisted(String accessToken);

    boolean isRtBlacklisted(String refreshToken);

}
