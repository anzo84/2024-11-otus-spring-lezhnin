package ru.otus.hw.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    @ModelAttribute("requestURI")
    public String contextPath(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("$")
    public I18nResolver setLocale(final HttpServletRequest request) {
        return new I18nResolver(messageSource, localeResolver.resolveLocale(request));
    }
}
