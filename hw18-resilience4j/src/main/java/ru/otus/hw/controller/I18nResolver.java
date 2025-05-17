package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;

import java.util.Locale;

@RequiredArgsConstructor
public class I18nResolver {

    private final MessageSource messageSource;

    private final Locale locale;

    public String msg(String key) throws NoSuchMessageException {
        return messageSource.getMessage(key, null, locale);
    }

    public String msg(String key, @Nullable Object[] args) throws NoSuchMessageException {
        return messageSource.getMessage(key, args, locale);
    }
}
