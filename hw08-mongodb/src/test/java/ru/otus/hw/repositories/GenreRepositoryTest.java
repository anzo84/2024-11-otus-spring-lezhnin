package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MONGODB для работы с жанрами")
@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен найти все жанры")
    @Test
    void shouldFindAllGenres() {
        assertThat(repository.findAll())
            .hasSize(3)
            .map(Genre::getName)
            .contains("Genre-1", "Genre-2", "Genre-3");
    }

    @DisplayName("должен найти жанры по идентификаторам")
    @Test
    void shouldFindGenreByIds() {
        assertThat(repository.findAllByIdIn(Set.of("1", "3")))
            .hasSize(2)
            .map(Genre::getName)
            .contains("Genre-1", "Genre-3");
    }

}