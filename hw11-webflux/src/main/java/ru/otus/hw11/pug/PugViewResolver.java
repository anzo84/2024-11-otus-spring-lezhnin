package ru.otus.hw11.pug;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PugViewResolver implements ViewResolver {

    private final ReactivePugRenderer renderer;

    @Override
    public Mono<View> resolveViewName(String viewName, Locale locale) {
        if (viewName.endsWith(".pug")) {
            return Mono.just(new PugView(viewName, renderer));
        }
        return Mono.empty();
    }
}