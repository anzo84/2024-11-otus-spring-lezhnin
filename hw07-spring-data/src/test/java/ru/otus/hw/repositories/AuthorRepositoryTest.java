package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами")
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен найти всех авторов")
    @Test
    void shouldFindAllAuthors() {
        assertThat(repository.findAll())
            .hasSize(3);
    }

    @DisplayName("должен найти автора по id")
    @Test
    void shouldFindAuthorById() {
        long id = 1L;
        Author expected = em.find(Author.class, id);
        Optional<Author> actual = repository.findById(id);
        assertThat(actual)
            .isNotEmpty()
            .usingRecursiveComparison()
            .isEqualTo(Optional.of(expected));
    }

    @DisplayName("должен вернуть пустой Optional")
    @Test
    void shouldNotFindAuthor() {
        Optional<Author> actual = repository.findById(200L);
        assertThat(actual).isEmpty();
    }

}
