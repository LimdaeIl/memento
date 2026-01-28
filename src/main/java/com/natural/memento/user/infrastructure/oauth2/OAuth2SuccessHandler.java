package com.natural.memento.user.infrastructure.oauth2;

import com.natural.memento.user.application.service.OAuth2AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthService oAuth2AuthService;

    @Value("${app.oauth2.success-redirect}")
    private String redirectUri;

    @Value("${app.oauth2.exchange-ttl-seconds:120}")
    private long ttlSeconds;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        OidcUser principal = (OidcUser) authentication.getPrincipal();

        String code = oAuth2AuthService.handleGoogle(
                principal.getEmail(),
                principal.getSubject(),
                principal.getFullName(),
                Duration.ofSeconds(ttlSeconds)
        );

        response.sendRedirect(redirectUri + "?code=" + code);
    }
}