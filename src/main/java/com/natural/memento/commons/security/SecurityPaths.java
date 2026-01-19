package com.natural.memento.commons.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityPaths {

    public static final String[] PUBLIC = {
            "/api/v1/auth/**",
            "/actuator/**",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    public static final String[] ADMIN = {
            "/admin/**"
    };
}