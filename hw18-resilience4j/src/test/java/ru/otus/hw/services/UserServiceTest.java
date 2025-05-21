package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.domain.service.UserService;
import ru.otus.hw.domain.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@DisplayName("Интеграционный тест сервиса пользователей")
@Import({UserServiceImpl.class})
@ComponentScan(basePackages = "ru.otus.hw.mapper")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    public PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        when(passwordEncoder.encode(anyString())).thenReturn("123456");
    }

    @Test
    @DisplayName("должен найти существующего пользователя по ID")
    void shouldFindUserById() {
        Optional<User> user = userService.findById(1L);
        assertThat(user)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(new User(1L, "admin", null, List.of(Role.ADMINISTRATOR)));
    }

    @Test
    @DisplayName("должен вернуть пустой Optional при поиске несуществующего пользователя")
    void shouldReturnEmptyOptionalForUnknownUser() {
        Optional<User> user = userService.findById(100L);
        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("должен возвращать список всех пользователей")
    void shouldReturnAllUsers() {
        List<User> users = userService.findAll();
        assertThat(users)
            .isNotEmpty()
            .map(User::getUsername)
            .contains("admin");
    }

    @Test
    @DisplayName("должен добавить нового пользователя")
    void shouldInsertNewUser() {
        User newUser = new User(null, "newUser", "newPassword", List.of(Role.COMMENTATOR));
        User savedUser = userService.save(newUser);
        newUser.setPassword(null);

        Optional<User> foundUser = userService.findById(savedUser.getId());
        assertThat(foundUser)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(newUser);
    }

    @Test
    @DisplayName("должен бросить исключение при попытке создать пользователя без ролей")
    void shouldThrowExceptionForInsertWithEmptyRoles() {
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> userService.save(new User(null, "newUser", "password", List.of())));
        assertThat(e.getMessage()).isEqualTo("User roles must not be empty");
    }

    @Test
    @DisplayName("должен бросить исключение при попытке создать пользователя с null ролями")
    void shouldThrowExceptionForInsertWithNullRoles() {
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> userService.save(new User(null, "newUser", "password", null)));
        assertThat(e.getMessage()).isEqualTo("User roles must not be empty");
    }

    @Test
    @DisplayName("должен обновить данные пользователя")
    void shouldUpdateUser() {
        User userToUpdate = new User(1L, "updatedUser", "updatedPassword", List.of(Role.AUTHOR));
        userService.save(userToUpdate);
        userToUpdate.setPassword(null);

        Optional<User> updatedUser = userService.findById(1L);
        assertThat(updatedUser)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(userToUpdate);
    }

    @Test
    @DisplayName("должен удалить пользователя")
    void shouldDeleteUser() {
        long userIdToDelete = 1L;
        userService.delete(userIdToDelete);

        Optional<User> deletedUser = userService.findById(userIdToDelete);
        assertThat(deletedUser).isEmpty();
    }
}