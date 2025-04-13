package ru.otus.hw.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.hw.persistence.model.UserEntity;
import ru.otus.hw.persistence.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DefaultUserCreator {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.default.user.name:admin}")
    private String username;

    @Value("${app.default.user.password:123456}")
    private String password;

    @PostConstruct
    public void init() {
        createDefaultUser();
    }

    @Scheduled(initialDelay = 60 * 1000, fixedRate = 60 * 1000)
    public void createDefaultUser() {
        if (userRepository.count() == 0) {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }
}
