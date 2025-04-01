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
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.AuthorServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@DisplayName("Реактивный тест сервиса авторов")
@ComponentScan(basePackages = "ru.otus.hw.mapper")
@Import({AuthorServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("должен вернуть всех авторов")
    void shouldReturnAllAuthors() {
        Flux<Author> authorsFlux = authorService.findAll();

        StepVerifier.create(authorsFlux.collectList())
            .assertNext(authors -> {
                assertThat(authors).isNotEmpty();
                assertThat(authors).extracting(Author::getFullName)
                    .contains("Author_1", "Author_2", "Author_3");
            })
            .verifyComplete();
    }
}