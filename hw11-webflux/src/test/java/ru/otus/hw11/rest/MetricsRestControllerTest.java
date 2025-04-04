package ru.otus.hw11.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otus.hw11.config.TestConfig;
import ru.otus.hw11.domain.service.AuthorService;
import ru.otus.hw11.domain.service.BookService;
import ru.otus.hw11.domain.service.CommentService;
import ru.otus.hw11.domain.service.GenreService;

import static org.mockito.BDDMockito.given;

@WebFluxTest(MetricsRestController.class)
@Import(TestConfig.class)
class MetricsRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("GET /api/metrics - получение метрик")
    void shouldGetMetricsAndReturnOk() {
        given(authorService.count()).willReturn(Mono.just(10L));
        given(bookService.count()).willReturn(Mono.just(20L));
        given(genreService.count()).willReturn(Mono.just(30L));
        given(commentService.count()).willReturn(Mono.just(40L));

        webTestClient.get()
            .uri("/api/metrics")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[?(@.name == 'authorCount')].value").isEqualTo(10)
            .jsonPath("$[?(@.name == 'bookCount')].value").isEqualTo(20)
            .jsonPath("$[?(@.name == 'genreCount')].value").isEqualTo(30)
            .jsonPath("$[?(@.name == 'commentCount')].value").isEqualTo(40);
    }
}