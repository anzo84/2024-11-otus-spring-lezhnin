package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интегро тест сервиса книг")
@Import({BookServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @Order(1)
    @DisplayName("должен найти существующую книгу по ID")
    public void shouldFindBookById() {
        Optional<Book> book = bookService.findById(1L);
        assertThat(book)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(
                Optional.of(
                    new Book(1L, "BookTitle_1",
                        new Author(1L, "Author_1"),
                        List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")))));
    }

    @Test
    @Order(2)
    @DisplayName("должен вернуть пустой Optional при поиске несуществующий книги")
    public void shouldReturnEmptyOptionalForUnknownBook() {
        Optional<Book> book = bookService.findById(100L);
        assertThat(book).isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("должен возвращать список всех книг")
    public void shouldReturnAllBooks() {
        var books = bookService.findAll();
        assertThat(books)
            .isNotEmpty()
            .map(Book::getTitle)
            .contains("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }

    @Test
    @Order(4)
    @DisplayName("должен добавить новую книгу")
    public void shouldInsertNewBook() {
        Book savedBook = bookService.insert("BookTitleNew", 1L, Set.of(1L, 2L));

        Optional<Book> findedBook = bookService.findById(savedBook.getId());
        assertThat(findedBook)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(
                Optional.of(
                    new Book(savedBook.getId(), "BookTitleNew",
                        new Author(1L, "Author_1"),
                        List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")))));
    }

    @Test
    @Order(5)
    @DisplayName("должен бросить исключение при попытке создать книгу с неизвестным автором")
    public void shouldThrowExceptionForInsertThenUnknownAuthor() {
        Exception e = assertThrows(EntityNotFoundException.class,
            () -> bookService.insert("BookTitleNew", 100L, Set.of(1L, 2L)));
        assertThat(e.getMessage()).isEqualTo("Author with id 100 not found");
    }

    @Test
    @Order(6)
    @DisplayName("должен бросить исключение при попытке создать книгу с NULL набором жанров")
    public void shouldThrowExceptionForInsertThenNullGenres() {
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> bookService.insert("BookTitleNew", 1L, null));
        assertThat(e.getMessage()).isEqualTo("Genres ids must not be null");
    }

    @Test
    @Order(7)
    @DisplayName("должен бросить исключение при попытке создать книгу с пустым набором жанров")
    public void shouldThrowExceptionForInsertThenEmptyGenres() {
        Exception e = assertThrows(IllegalArgumentException.class,
            () -> bookService.insert("BookTitleNew", 1L, Set.of()));
        assertThat(e.getMessage()).isEqualTo("Genres ids must not be null");
    }

    @Test
    @Order(8)
    @DisplayName("должен бросить исключение при попытке создать книгу с неизвестным жанром")
    public void shouldThrowExceptionForInsertThenUnknownGenreId() {
        Exception e = assertThrows(EntityNotFoundException.class,
            () -> bookService.insert("BookTitleNew", 1L, Set.of(100L)));
        assertThat(e.getMessage()).isEqualTo("One or all genres with ids [100] not found");
    }

    @Test
    @Order(9)
    @DisplayName("должен обновить данные по книге")
    public void shouldUpdateBook() {
        bookService.update(1L, "BookTitleUpdated", 2L, Set.of(3L));
        Optional<Book> findedBook = bookService.findById(1L);
        assertThat(findedBook)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(
                Optional.of(
                    new Book(1L, "BookTitleUpdated", new Author(2L, "Author_2"), List.of(new Genre(3L, "Genre_3")))));
    }

    @Test
    @Order(10)
    @DisplayName("должен удалить книгу")
    public void shouldDeleteBook() {
        bookService.deleteById(4L);
        var books = bookService.findAll();
        assertThat(books)
            .isNotEmpty()
            .map(Book::getId)
            .contains(1L, 2L, 3L);
    }

}