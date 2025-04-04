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
import ru.otus.hw11.domain.model.Author;
import ru.otus.hw11.domain.model.Book;
import ru.otus.hw11.domain.model.Genre;
import ru.otus.hw11.domain.service.BookService;
import ru.otus.hw11.rest.model.AuthorDto;
import ru.otus.hw11.rest.model.BookDto;
import ru.otus.hw11.rest.model.GenreDto;
import ru.otus.hw11.rest.model.ModifyBookDto;
import ru.otus.hw11.config.TestConfig;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest(BookRestController.class)
@Import(TestConfig.class)
class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("POST /api/books - создание книги")
    void shouldCreateBookAndReturnOk() {
        AuthorDto authorDto = new AuthorDto("Test Author");
        authorDto.setId(1L);

        GenreDto genreDto = new GenreDto("Test Genre");
        genreDto.setId(1L);

        BookDto bookDto = new BookDto("Test Book", authorDto, List.of(genreDto));
        bookDto.setId(1L);

        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");
        Book book = new Book(1L, "Test Book", author, List.of(genre));

        given(bookService.save(any(Mono.class))).willReturn(Mono.just(book));

        webTestClient.post()
            .uri("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(bookDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.title").isEqualTo("Test Book")
            .jsonPath("$.author.fullName").isEqualTo("Test Author")
            .jsonPath("$.genres[0].name").isEqualTo("Test Genre");
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - удаление книги")
    void shouldDeleteBookAndReturnOk() {
        Long id = 1L;
        given(bookService.delete(id)).willReturn(Mono.empty());

        webTestClient.delete()
            .uri("/api/books/{id}", id)
            .exchange()
            .expectStatus().isNoContent();

        verify(bookService, times(1)).delete(id);
    }

    @Test
    @DisplayName("GET /api/books - получение всех книг")
    void shouldGetAllBooksAndReturnOk() {
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");
        Book book = new Book(1L, "Test Book", author, List.of(genre));

        given(bookService.findAll()).willReturn(Flux.just(book));

        webTestClient.get()
            .uri("/api/books")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(1L)
            .jsonPath("$[0].title").isEqualTo("Test Book")
            .jsonPath("$[0].author.fullName").isEqualTo("Test Author")
            .jsonPath("$[0].genres[0].name").isEqualTo("Test Genre");
    }

    @Test
    @DisplayName("GET /api/books/{id} - получение книги по ID (найден)")
    void shouldGetBookByIdAndReturnOkWhenBookExists() {
        Long id = 1L;
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");
        Book book = new Book(id, "Test Book", author, List.of(genre));

        given(bookService.findById(id)).willReturn(Mono.just(book));

        webTestClient.get()
            .uri("/api/books/{id}", id)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.title").isEqualTo("Test Book")
            .jsonPath("$.author.fullName").isEqualTo("Test Author")
            .jsonPath("$.genres[0].name").isEqualTo("Test Genre");
    }

    @Test
    @DisplayName("GET /api/books/{id} - получение книги по ID (не найден)")
    void shouldReturnNotFoundWhenBookDoesNotExist() {
        Long id = 1L;
        given(bookService.findById(id)).willReturn(Mono.empty());

        webTestClient.get()
            .uri("/api/books/{id}", id)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    @DisplayName("PUT /api/books/{id} - обновление книги")
    void shouldUpdateBookAndReturnOk() {
        Long id = 1L;
        AuthorDto authorDto = new AuthorDto("Test Author");
        authorDto.setId(1L);

        GenreDto genreDto = new GenreDto("Test Genre");
        genreDto.setId(1L);

        ModifyBookDto updateBookRequestDto = new ModifyBookDto();
        updateBookRequestDto.setTitle("Updated Book");
        updateBookRequestDto.setAuthor(authorDto);
        updateBookRequestDto.setGenres(List.of(genreDto));

        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");
        Book book = new Book(id, "Updated Book", author, List.of(genre));

        given(bookService.save(any(Mono.class))).willReturn(Mono.just(book));

        webTestClient.put()
            .uri("/api/books/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateBookRequestDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.title").isEqualTo("Updated Book")
            .jsonPath("$.author.fullName").isEqualTo("Test Author")
            .jsonPath("$.genres[0].name").isEqualTo("Test Genre");
    }
}