package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами")
@DataJpaTest
@Import({GenreRepositoryImpl.class})
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен найти все жанры")
    @Test
    void shouldFindAllGenres() {
        assertThat(repository.findAll())
            .hasSize(6)
            .map(Genre::getName)
            .contains("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6");
    }

    @DisplayName("должен найти жанры по идентификаторам")
    @Test
    void shouldFindGenreByIds() {
        assertThat(repository.findAllByIds(Set.of(1L, 6L)))
            .hasSize(2)
            .map(Genre::getName)
            .contains("Genre_1", "Genre_6");
    }

}