package ru.otus.hw11.config;

import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.spring.template.SpringTemplateLoader;
import de.neuland.pug4j.spring.view.PugViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.neuland.pug4j.Pug4J.Mode.HTML;

@Configuration
public class PugConfig {

    @Bean
    public SpringTemplateLoader templateLoader() {
        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setTemplateLoaderPath("classpath:/templates");
        templateLoader.setEncoding("UTF-8");
        templateLoader.setSuffix(".pug");
        return templateLoader;
    }

    @Bean
    public PugConfiguration pugConfiguration(SpringTemplateLoader templateLoader) {
        PugConfiguration configuration = new PugConfiguration();
        configuration.setCaching(false);
        configuration.setTemplateLoader(templateLoader);
        configuration.setPrettyPrint(true);
        configuration.setMode(HTML);
        return configuration;
    }
}
