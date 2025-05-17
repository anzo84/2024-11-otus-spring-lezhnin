package ru.otus.hw.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.hw.persistence.model.RoleAlias;
import ru.otus.hw.persistence.model.RoleEntity;
import ru.otus.hw.persistence.model.UserEntity;
import ru.otus.hw.persistence.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DefaultUserCreator {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.default.users.administrator.name:admin}")
    private String adminUsername;

    @Value("${app.default.users.administrator.password:123456}")
    private String adminPassword;

    @Value("${app.default.users.author.name:author}")
    private String authorUsername;

    @Value("${app.default.users.author.password:123456}")
    private String authorPassword;

    @Value("${app.default.users.commentator.name:commentator}")
    private String commentatorUsername;

    @Value("${app.default.users.commentator.password:123456}")
    private String commentatorPassword;

    @PostConstruct
    public void init() {
        createDefaultUsers();
    }

    @Scheduled(initialDelay = 60 * 1000, fixedRate = 60 * 1000)
    public void createDefaultUsers() {
        if (userRepository.count() == 0) {
            createUser(adminUsername, adminPassword, RoleAlias.ADMINISTRATOR);
            createUser(authorUsername, authorPassword, RoleAlias.AUTHOR);
            createUser(commentatorUsername, commentatorPassword, RoleAlias.COMMENTATOR);
        }
    }

    private void createUser(String username, String password, RoleAlias alias) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        RoleEntity role = new RoleEntity();
        role.setUser(user);
        role.setAlias(alias);
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
