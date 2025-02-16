package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.AuthorServiceImpl;
import ru.otus.hw.persistence.model.AuthorEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интегро тест сервиса авторов")
@Import({AuthorServiceImpl.class})
class AuthorEntityServiceTest {

    @Autowired
    private AuthorService authorService;

    @Test
    public void shouldReturnAllGenres() {
        /* assertThat(authorService.findAll())
            .isNotEmpty()
            .map(AuthorEntity::getFullName)
            .contains("Author_1", "Author_2", "Author_3"); */
    }
}