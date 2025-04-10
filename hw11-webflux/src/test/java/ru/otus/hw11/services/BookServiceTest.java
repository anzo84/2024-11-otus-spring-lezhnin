package ru.otus.hw11.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw11.domain.model.Author;
import ru.otus.hw11.domain.model.Book;
import ru.otus.hw11.domain.model.Genre;
import ru.otus.hw11.domain.service.BookService;
import ru.otus.hw11.domain.service.BookServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataR2dbcTest
@DisplayName("Реактивный тест сервиса книг")
@ComponentScan(basePackages = "ru.otus.hw11.mapper")
@Import({BookServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @Order(1)
    @DisplayName("должен найти существующую книгу по ID")
    void shouldFindBookById() {
        Book expectedBook = new Book(1L, "BookTitle_1",
            new Author(1L, "Author_1"),
            List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));

        StepVerifier.create(bookService.findById(1L))
            .assertNext(book -> assertThat(book)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedBook))
            .verifyComplete();
    }

    @Test
    @Order(2)
    @DisplayName("должен вернуть пустой Mono при поиске несуществующей книги")
    void shouldReturnEmptyMonoForUnknownBook() {
        StepVerifier.create(bookService.findById(100L))
            .verifyComplete();
    }

    @Test
    @Order(3)
    @DisplayName("должен возвращать список всех книг с авторами и жанрами")
    void shouldReturnAllBooksWithAuthorsAndGenres() {
        StepVerifier.create(bookService.findAll().collectList())
            .assertNext(books -> {
                // Проверка структуры данных
                assertThat(books)
                    .extracting(
                        Book::getTitle,
                        b -> b.getAuthor().getFullName(),
                        b -> b.getGenres().stream().map(Genre::getName).collect(Collectors.toList())
                    )
                    .containsExactlyInAnyOrder(
                        tuple("BookTitle_1", "Author_1", List.of("Genre_1", "Genre_2")),
                        tuple("BookTitle_2", "Author_2", List.of("Genre_3", "Genre_4")),
                        tuple("BookTitle_3", "Author_3", List.of("Genre_5", "Genre_6"))
                    );
            })
            .verifyComplete();
    }

    @Test
    @Order(4)
    @DisplayName("должен добавить новую книгу")
    void shouldInsertNewBook() {
        Book newBook = new Book(null, "BookTitleNew",
            new Author(1L, "Author_1"),
            List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));

        StepVerifier.create(bookService.save(Mono.just(newBook)))
            .assertNext(savedBook -> {
                assertThat(savedBook.getId()).isNotNull();
                assertThat(savedBook.getTitle()).isEqualTo("BookTitleNew");
                assertThat(savedBook.getAuthor().getId()).isEqualTo(1L);
                assertThat(savedBook.getGenres()).hasSize(2);
            })
            .verifyComplete();
    }

    @Test
    @Order(5)
    @DisplayName("должен обновить данные по книге")
    void shouldUpdateBook() {
        Book updatedBook = new Book(1L, "BookTitleUpdated",
            new Author(2L, "Author_2"),
            List.of(new Genre(3L, "Genre_3")));

        StepVerifier.create(bookService.save(Mono.just(updatedBook)))
            .assertNext(book -> {
                assertThat(book.getId()).isEqualTo(1L);
                assertThat(book.getTitle()).isEqualTo("BookTitleUpdated");
                assertThat(book.getAuthor().getId()).isEqualTo(2L);
                assertThat(book.getGenres()).hasSize(1);
            })
            .verifyComplete();
    }

    @Test
    @Order(6)
    @DisplayName("должен удалить книгу")
    void shouldDeleteBook() {
        StepVerifier.create(bookService.delete(1L))
            .verifyComplete();

        StepVerifier.create(bookService.findAll().collectList())
            .assertNext(books -> {
                assertThat(books).hasSize(3);
                assertThat(books)
                    .extracting(Book::getId)
                    .containsExactlyInAnyOrder(2L, 3L, 4L);
            })
            .verifyComplete();
    }

    @Test
    @Order(7)
    @DisplayName("должен вернуть количество книг")
    void shouldReturnBookCount() {
        StepVerifier.create(bookService.count())
            .expectNext(3L)
            .verifyComplete();
    }

}