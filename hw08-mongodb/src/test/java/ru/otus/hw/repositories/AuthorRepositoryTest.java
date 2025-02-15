package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MONGODB для работы с авторами")
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен найти всех авторов")
    @Test
    void shouldFindAllAuthors() {
        assertThat(repository.findAll())
            .hasSize(3);
    }

    @DisplayName("должен найти автора по id")
    @Test
    void shouldFindAuthorById() {
        String id = "1";
        Optional<Author> expected = Optional.ofNullable(mongoTemplate.findById(id, Author.class));
        Optional<Author> actual = repository.findById(id);
        assertThat(actual)
            .isNotEmpty()
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("должен вернуть пустой Optional")
    @Test
    void shouldNotFindAuthor() {
        Optional<Author> actual = repository.findById("1000");
        assertThat(actual).isEmpty();
    }
}
