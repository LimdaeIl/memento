package com.natural.memento.commons.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityPaths {

    public static final String[] PUBLIC = {
            "/api/v1/auth/**",
            "/oauth2/**",
            "/login/oauth2/**",
            "/oauth2/authorization/google",
            "/login/oauth2/code/google",
            "/actuator/**",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    public static final String[] ADMIN = {
            "/admin/**"
    };
}
