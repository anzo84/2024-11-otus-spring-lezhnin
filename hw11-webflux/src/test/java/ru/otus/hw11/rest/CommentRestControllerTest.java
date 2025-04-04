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
import ru.otus.hw11.domain.model.Comment;
import ru.otus.hw11.domain.service.CommentService;
import ru.otus.hw11.rest.model.CommentDto;
import ru.otus.hw11.rest.model.ModifyCommentDto;
import ru.otus.hw11.config.TestConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest(CommentRestController.class)
@Import(TestConfig.class)
class CommentRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("POST /api/comments - создание комментария")
    void shouldCreateCommentAndReturnOk() {
        CommentDto commentDto = new CommentDto("Test Comment", null);
        commentDto.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        given(commentService.save(any(Mono.class))).willReturn(Mono.just(comment));

        webTestClient.post()
            .uri("/api/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(commentDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.content").isEqualTo("Test Comment");
    }

    @Test
    @DisplayName("DELETE /api/comments/{id} - удаление комментария")
    void shouldDeleteCommentAndReturnOk() {
        Long id = 1L;
        given(commentService.delete(id)).willReturn(Mono.empty());

        webTestClient.delete()
            .uri("/api/comments/{id}", id)
            .exchange()
            .expectStatus().isNoContent();

        verify(commentService, times(1)).delete(id);
    }

    @Test
    @DisplayName("GET /api/comments - получение всех комментариев для книги")
    void shouldGetAllCommentsAndReturnOk() {
        Long bookId = 1L;
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        given(commentService.findByBookId(bookId)).willReturn(Flux.just(comment));

        webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/api/comments")
                .queryParam("bookId", bookId)
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(1L)
            .jsonPath("$[0].content").isEqualTo("Test Comment");
    }

    @Test
    @DisplayName("GET /api/comments/{id} - получение комментария по ID (найден)")
    void shouldGetCommentByIdAndReturnOkWhenCommentExists() {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("Test Comment");

        given(commentService.findById(id)).willReturn(Mono.just(comment));

        webTestClient.get()
            .uri("/api/comments/{id}", id)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.content").isEqualTo("Test Comment");
    }

    @Test
    @DisplayName("GET /api/comments/{id} - получение комментария по ID (не найден)")
    void shouldReturnNotFoundWhenCommentDoesNotExist() {
        Long id = 1L;
        given(commentService.findById(id)).willReturn(Mono.empty());

        webTestClient.get()
            .uri("/api/comments/{id}", id)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @DisplayName("PUT /api/comments/{id} - обновление комментария")
    void shouldUpdateCommentAndReturnOk() {
        Long id = 1L;
        ModifyCommentDto updateCommentRequestDto = new ModifyCommentDto();
        updateCommentRequestDto.setContent("Updated Comment");
        updateCommentRequestDto.setBook(null);

        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("Updated Comment");

        given(commentService.save(any(Mono.class))).willReturn(Mono.just(comment));

        webTestClient.put()
            .uri("/api/comments/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateCommentRequestDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
            .jsonPath("$.content").isEqualTo("Updated Comment");
    }
}