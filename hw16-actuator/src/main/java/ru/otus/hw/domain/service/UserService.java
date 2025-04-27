package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import ru.otus.hw.domain.model.RoleDescription;
import ru.otus.hw.domain.model.User;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(long id);

    List<User> findAll();

    Long count();

    User save(@Valid User user);

    void delete(Long id);

    List<RoleDescription> getRoleDescriptions(Locale locale);
}
