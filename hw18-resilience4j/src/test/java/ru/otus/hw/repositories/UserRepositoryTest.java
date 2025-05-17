package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.persistence.model.RoleAlias;
import ru.otus.hw.persistence.model.RoleEntity;
import ru.otus.hw.persistence.model.UserEntity;
import ru.otus.hw.persistence.repository.UserRepository;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с пользователями")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен найти пользователя по имени")
    @Test
    void shouldFindByUsername() {
        String username = "admin";
        UserEntity expected = testEntityManager.find(UserEntity.class, 1L);
        Optional<UserEntity> actual = repository.findByUsername(username);

        assertThat(actual)
            .isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .ignoringFields("roles.user") // Игнорируем циклическую зависимость
            .isEqualTo(expected);
    }

    @DisplayName("должен вернуть пустой Optional при поиске несуществующего пользователя")
    @Test
    void shouldNotFindNonExistentUser() {
        Optional<UserEntity> actual = repository.findByUsername("nonExistentUser");
        assertThat(actual).isEmpty();
    }

    @DisplayName("должен сохранять нового пользователя с ролями")
    @Test
    void shouldSaveNewUserWithRoles() {
        UserEntity newUser = new UserEntity();
        newUser.setUsername("newUser");
        newUser.setPassword("password");

        RoleEntity role = new RoleEntity();
        role.setAlias(RoleAlias.COMMENTATOR);
        role.setUser(newUser);
        newUser.getRoles().add(role);

        UserEntity saved = repository.save(newUser);
        testEntityManager.flush();
        testEntityManager.clear();

        UserEntity found = testEntityManager.find(UserEntity.class, saved.getId());
        assertThat(found)
            .isNotNull()
            .extracting(UserEntity::getUsername)
            .isEqualTo("newUser");

        assertThat(found.getRoles())
            .hasSize(1)
            .first()
            .extracting(RoleEntity::getAlias)
            .isEqualTo(RoleAlias.COMMENTATOR);
    }

    @DisplayName("должен обновлять существующего пользователя")
    @Test
    void shouldUpdateExistingUser() {
        Long userId = 1L;
        UserEntity user = testEntityManager.find(UserEntity.class, userId);
        user.setUsername("updatedUsername");

        repository.save(user);
        testEntityManager.flush();
        testEntityManager.clear();

        UserEntity found = testEntityManager.find(UserEntity.class, userId);
        assertThat(found.getUsername()).isEqualTo("updatedUsername");
    }
}