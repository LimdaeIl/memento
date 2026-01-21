package com.natural.memento.user.infrastructure.email;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.email")
public record AuthEmailProperties(
        Duration codeValidityDuration,
        Duration resendCooldownDuration,
        int maxAttempts,
        Duration lockoutDuration,
        Duration successValidityDuration,
        Sender sender,
        Template template
) {

}
