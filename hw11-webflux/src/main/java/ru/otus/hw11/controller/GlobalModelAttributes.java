package ru.otus.hw11.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final MessageSource messageSource;

    @ModelAttribute
    public Mono<String> requestURI(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getPath().toString());
    }

    @ModelAttribute("$")
    public I18nResolver i18nResolver(ServerWebExchange exchange) {
        return new I18nResolver(messageSource, exchange.getLocaleContext().getLocale());
    }
}