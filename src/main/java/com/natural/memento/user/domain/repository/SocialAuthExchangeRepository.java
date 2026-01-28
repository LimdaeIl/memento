package com.natural.memento.user.domain.repository;

import java.time.Duration;

public interface SocialAuthExchangeRepository {
    String issue(Long userId, Duration ttl);
    Long consume(String code); // 없거나 만료면 null
}
