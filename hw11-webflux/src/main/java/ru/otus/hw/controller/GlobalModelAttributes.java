package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    /*
    private final MessageSource messageSource;

    private final LocaleResolver localeResolver;

    @ModelAttribute("requestURI")
    public String contextPath(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("$")
    public I18nResolver setLocale(final HttpServletRequest request) {
        return new I18nResolver(messageSource, localeResolver.resolveLocale(request));
    }*/
}
