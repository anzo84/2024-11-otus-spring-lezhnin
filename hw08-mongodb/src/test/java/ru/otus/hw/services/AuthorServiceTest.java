package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Интегро тест сервиса авторов")
@Import({AuthorServiceImpl.class})
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    public void shouldReturnAllGenres() {
        assertThat(authorService.findAll())
            .isNotEmpty()
            .map(Author::getFullName)
            .contains("Author-1", "Author-2", "Author-3");
    }
}