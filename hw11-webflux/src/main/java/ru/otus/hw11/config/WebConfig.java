package ru.otus.hw11.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import ru.otus.hw11.pug.PugViewResolver;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebFluxConfigurer {

    private final PugViewResolver pugViewResolver;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(pugViewResolver);
    }
}
