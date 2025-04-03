package ru.otus.hw.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18nConfiguration {

    /*
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }*/

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(30); //refresh cache once per 30 sec
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames("classpath:i18n/common",
            "classpath:i18n/genre",
            "classpath:i18n/book",
            "classpath:i18n/genre",
            "classpath:i18n/author",
            "classpath:i18n/comment");
        return messageSource;
    }

    /*
    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }*/
}