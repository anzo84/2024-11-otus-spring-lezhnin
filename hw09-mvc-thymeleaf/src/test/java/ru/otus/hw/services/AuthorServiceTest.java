package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.AuthorServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интегро тест сервиса авторов")
@ComponentScan(basePackages = "ru.otus.hw.mapper")
@Import({AuthorServiceImpl.class})
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    public void shouldReturnAllGenres() {
         assertThat(authorService.findAll())
            .isNotEmpty()
            .map(Author::getFullName)
            .contains("Author_1", "Author_2", "Author_3");
    }
}