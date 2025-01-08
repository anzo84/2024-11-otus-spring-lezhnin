package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.hw.service.IOService;

import java.util.Map;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {"ru.otus.hw.service", "ru.otus.hw.dao"})
public class TestConfiguration {

    @Bean
    @Qualifier("streamsIOService")
    @Primary
    public IOService testIOService() {
        return mock(IOService.class);
    }

    @Bean
    @Primary
    public AppProperties testAppProperties() {
        return new AppProperties(3, "test", Map.of("test", "questions.csv"));
    }
}