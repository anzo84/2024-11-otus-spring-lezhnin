package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.domain.model.Role;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] ANONYMOUS_MATCHERS = new String[]{
        "/login"
    };

    private static final String[] PERMIT_ALL_MATCHERS = new String[]{
        "/css/**", "/js/**", "/img/**", "/actuator/**"
    };

    private static final String[] AUTHENTICATED_MATCHERS = new String[]{
        "/author/**", "/api/authors/**",
        "/genre/**", "/api/genres/**",
        "/book/**", "/api/books/**",
        "/comment/**", "/api/comments/**",
        "/user/**", "/api/users/**",
        "/", "/home/**", "/api/metrics/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(ANONYMOUS_MATCHERS).anonymous()
                .requestMatchers(PERMIT_ALL_MATCHERS).permitAll()
                .requestMatchers(AUTHENTICATED_MATCHERS).authenticated()
                .requestMatchers("/datarest/**").hasRole("ADMINISTRATOR")
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/home", true) // Перенаправление после успешного логина
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =  new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = Arrays.stream(Role.values()).map(Role::getAuthority).collect(Collectors.joining(" > "));
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}

