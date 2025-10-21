package com.g2.language;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class MessageResolver {

    private final MessageSource messageSource;

    public String resolve(MessageKey messageKey, Locale locale) {
        return messageSource.getMessage(messageKey.getBundleKey(), null, locale);
    }
}
