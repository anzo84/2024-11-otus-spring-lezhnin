package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.domain.service.GenreServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интегро тест сервиса жанров")
@Import({GenreServiceImpl.class})
class GenreEntityServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    public void shouldReturnAllGenres() {
        assertThat(genreService.findAll())
            .isNotEmpty()
            .map(Genre::getName)
            .contains("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6");
    }
}