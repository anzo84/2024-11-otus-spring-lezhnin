package ru.otus.hw11.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.model.Genre;
import ru.otus.hw11.domain.service.GenreService;
import ru.otus.hw11.rest.model.GenreDto;
import ru.otus.hw11.rest.model.ModifyGenreDto;
import ru.otus.hw11.config.TestConfig;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest(GenreRestController.class)
@Import(TestConfig.class)
class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("POST /api/genres - создание жанра")
    void shouldCreateGenreAndReturnOk() {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Test Genre");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        given(genreService.save(any(Mono.class))).willReturn(Mono.just(genre));

        webTestClient.post()
            .uri("/api/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(genreDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.name").isEqualTo("Test Genre");
    }

    @Test
    @DisplayName("DELETE /api/genres/{id} - удаление жанра")
    void shouldDeleteGenreAndReturnOk() {
        Long id = 1L;
        given(genreService.delete(id)).willReturn(Mono.empty());

        webTestClient.delete()
            .uri("/api/genres/{id}", id)
            .exchange()
            .expectStatus().isNoContent();

        verify(genreService, times(1)).delete(id);
    }

    @Test
    @DisplayName("GET /api/genres - получение всех жанров")
    void shouldGetAllGenresAndReturnOk() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        given(genreService.findAll()).willReturn(Flux.just(genre));

        webTestClient.get()
            .uri("/api/genres")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(1L)
            .jsonPath("$[0].name").isEqualTo("Test Genre");
    }

    @Test
    @DisplayName("GET /api/genres/{id} - получение жанра по ID (найден)")
    void shouldGetGenreByIdAndReturnOkWhenGenreExists() {
        Long id = 1L;
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName("Test Genre");

        given(genreService.findById(id)).willReturn(Mono.just(genre));

        webTestClient.get()
            .uri("/api/genres/{id}", id)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.name").isEqualTo("Test Genre");
    }

    @Test
    @DisplayName("GET /api/genres/{id} - получение жанра по ID (не найден)")
    void shouldReturnNotFoundWhenGenreDoesNotExist() {
        Long id = 1L;
        given(genreService.findById(id)).willReturn(Mono.empty());

        webTestClient.get()
            .uri("/api/genres/{id}", id)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @DisplayName("PUT /api/genres/{id} - обновление жанра")
    void shouldUpdateGenreAndReturnOk() {
        Long id = 1L;
        ModifyGenreDto updateGenreRequestDto = new ModifyGenreDto();
        updateGenreRequestDto.setName("Updated Genre");

        Genre genre = new Genre();
        genre.setId(id);
        genre.setName("Updated Genre");

        given(genreService.save(any(Mono.class))).willReturn(Mono.just(genre));

        webTestClient.put()
            .uri("/api/genres/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateGenreRequestDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.name").isEqualTo("Updated Genre");
    }

    @Test
    @DisplayName("GET /api/genres/by-ids - получение жанров по списку ID")
    void shouldGetGenresByIdsAndReturnOk() {
        List<Long> ids = List.of(1L, 2L);
        Genre genre1 = new Genre(1L, "Genre 1");
        Genre genre2 = new Genre(2L, "Genre 2");

        given(genreService.getGenresByIds(ids)).willReturn(Flux.just(genre1, genre2));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/api/genres/by-ids")
                .queryParam("ids", ids)
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(1L)
            .jsonPath("$[0].name").isEqualTo("Genre 1")
            .jsonPath("$[1].id").isEqualTo(2L)
            .jsonPath("$[1].name").isEqualTo("Genre 2");
    }
}