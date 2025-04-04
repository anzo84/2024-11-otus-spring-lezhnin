package ru.otus.hw11.rest;

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
import ru.otus.hw11.config.TestConfig;
import ru.otus.hw11.domain.model.Author;
import ru.otus.hw11.domain.service.AuthorService;
import ru.otus.hw11.rest.model.AuthorDto;
import ru.otus.hw11.rest.model.ModifyAuthorDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = {AuthorRestController.class})
@Import(TestConfig.class)
class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("POST /api/authors - создание автора")
    void shouldCreateAuthorAndReturnOk() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setFullName("Test Author");

        Author author = new Author();
        author.setId(1L);
        author.setFullName("Test Author");

        given(authorService.save(any(Mono.class))).willReturn(Mono.just(author));

        webTestClient.post().uri("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(authorDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.fullName").isEqualTo("Test Author");
    }

    @Test
    @DisplayName("DELETE /api/authors/{id} - удаление автора")
    void shouldDeleteAuthorAndReturnOk() {
        Long id = 1L;
        given(authorService.delete(id)).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/authors/{id}", id)
            .exchange()
            .expectStatus()
            .isNoContent();

        verify(authorService).delete(id);
    }

    @Test
    @DisplayName("GET /api/authors - получение всех авторов")
    void shouldGetAllAuthorsAndReturnOk() {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("Test Author");

        given(authorService.findAll()).willReturn(Flux.just(author));

        webTestClient.get().uri("/api/authors")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(AuthorDto.class)
            .hasSize(1)
            .consumeWith(result -> {
                AuthorDto dto = result.getResponseBody().get(0);
                assert dto.getId() == 1L;
                assert dto.getFullName().equals("Test Author");
            });
    }

    @Test
    @DisplayName("GET /api/authors/{id} - получение автора по ID (найден)")
    void shouldGetAuthorByIdAndReturnOkWhenAuthorExists() {
        Long id = 1L;
        Author author = new Author();
        author.setId(id);
        author.setFullName("Test Author");

        given(authorService.findById(id)).willReturn(Mono.just(author));

        webTestClient.get().uri("/api/authors/{id}", id)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.fullName").isEqualTo("Test Author");
    }

    @Test
    @DisplayName("GET /api/authors/{id} - получение автора по ID (не найден)")
    void shouldReturnNotFoundWhenAuthorDoesNotExist() {
        Long id = 1L;
        given(authorService.findById(id)).willReturn(Mono.empty());

        webTestClient.get().uri("/api/authors/{id}", id)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    @DisplayName("PUT /api/authors/{id} - обновление автора")
    void shouldUpdateAuthorAndReturnOk() {
        Long id = 1L;
        ModifyAuthorDto updateAuthorRequestDto = new ModifyAuthorDto();
        updateAuthorRequestDto.setFullName("Updated Author");

        Author author = new Author();
        author.setId(id);
        author.setFullName("Updated Author");

        given(authorService.save(any(Mono.class))).willReturn(Mono.just(author));

        webTestClient.put().uri("/api/authors/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateAuthorRequestDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.fullName").isEqualTo("Updated Author");
    }
}