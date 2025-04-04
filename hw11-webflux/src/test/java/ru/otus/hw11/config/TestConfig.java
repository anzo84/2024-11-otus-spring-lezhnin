package ru.otus.hw11.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ru.otus.hw11.rest.mapper", "ru.otus.hw11.pug", "ru.otus.hw11.config"})
public class TestConfig {
}
