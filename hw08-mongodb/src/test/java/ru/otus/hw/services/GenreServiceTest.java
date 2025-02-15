package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Интегро тест сервиса жанров")
@Import({GenreServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    public void shouldReturnAllGenres() {
        assertThat(genreService.findAll())
            .isNotEmpty()
            .map(Genre::getName)
            .contains("Genre-1", "Genre-2", "Genre-3");
    }
}