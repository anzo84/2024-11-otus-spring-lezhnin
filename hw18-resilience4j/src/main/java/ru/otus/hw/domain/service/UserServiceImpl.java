package ru.otus.hw.domain.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.model.RoleDescription;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.persistence.model.RoleEntity;
import ru.otus.hw.persistence.model.UserEntity;
import ru.otus.hw.persistence.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
@PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).ADMINISTRATOR)")
@RateLimiter(name = "defaultRateLimiter")
@CircuitBreaker(name = "defaultCircuitBreaker")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final MessageSource messageSource;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id).map(userMapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll().stream().map(userMapper::map).collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public User save(@Valid User user) {
        if (Optional.ofNullable(user.getRoles()).map(List::size).orElse(0) == 0) {
            throw new IllegalArgumentException("User roles must not be empty");
        }

        UserEntity entity = user.getId() == null ? new UserEntity() :
            userRepository.findById(user.getId()).orElse(new UserEntity());
        entity.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        entity.getRoles().clear();
        user.getRoles().forEach(role -> {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setUser(entity);
            roleEntity.setAlias(userMapper.map(role));
            entity.getRoles().add(roleEntity);
        });
        return userMapper.map(userRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<RoleDescription> getRoleDescriptions(Locale locale) {
        return Arrays.stream(Role.values()).map(role -> {
            RoleDescription roleDescription = new RoleDescription();
            roleDescription.setRole(role);
            String msgName = "role." + role.name().toLowerCase();
            roleDescription.setDescription(messageSource.getMessage(msgName, null, locale));
            return roleDescription;
        }).collect(Collectors.toList());
    }
}
