package com.natural.memento.user.infrastructure.email.template;


import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Component
@RequiredArgsConstructor
public class EmailTemplateRenderer {

    private final SpringTemplateEngine templateEngine;

    public String render(String templateName, Map<String, Object> variables) {
        Context context = new org.thymeleaf.context.Context(Locale.KOREA);
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}