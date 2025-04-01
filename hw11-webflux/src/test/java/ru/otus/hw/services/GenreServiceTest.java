package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.domain.service.GenreServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@DisplayName("Реактивный тест сервиса жанров")
@ComponentScan(basePackages = "ru.otus.hw.mapper")
@Import({GenreServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("должен вернуть все жанры")
    void shouldReturnAllGenres() {
        Flux<Genre> genresFlux = genreService.findAll();

        StepVerifier.create(genresFlux.collectList())
            .assertNext(genres -> {
                assertThat(genres).isNotEmpty();
                assertThat(genres).extracting(Genre::getName)
                    .contains("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6");
            })
            .verifyComplete();
    }
}