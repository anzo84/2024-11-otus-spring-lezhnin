package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.service.IOService;

import static org.mockito.Mockito.mock;

@PropertySource("classpath:application.properties")
@Configuration
@ComponentScan(basePackages = "ru.otus.hw")
public class TestConfiguration {

  @Bean()
  @Primary
  public IOService testIOService() {
    return mock(IOService.class);
  }
}
