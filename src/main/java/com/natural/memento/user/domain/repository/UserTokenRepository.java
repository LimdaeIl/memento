package com.natural.memento.user.domain.repository;

public interface UserTokenRepository {

    void saveRefreshToken(Long userId, String refreshToken, long ttlMillis);

    String findRt(Long userId);

    void deleteRt(Long userId);

    void blacklistAt(String accessToken, long ttlMillis);

    void blacklistRt(String refreshToken, long ttlMillis);

    boolean isAtBlacklisted(String accessToken);

    boolean isRtBlacklisted(String refreshToken);

}
