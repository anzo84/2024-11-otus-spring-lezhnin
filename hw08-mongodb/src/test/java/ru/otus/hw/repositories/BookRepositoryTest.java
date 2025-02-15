package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий на основе MONGODB для работы с книгами")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository repository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        String authorId = "1";
        String genreId = "1";
        String bookTitle = "BookTitle_Test";
        Author author = mongoTemplate.findById(authorId, Author.class);
        Genre genre = mongoTemplate.findById(genreId, Genre.class);
        Book newBook = new Book();
        newBook.setAuthor(author);
        newBook.setGenres(Collections.singletonList(genre));
        newBook.setTitle(bookTitle);
        Book savedBook = repository.save(newBook);
        assertNotNull(savedBook);

        var book2 = mongoTemplate.findById(savedBook.getId(), Book.class);
        assertThat(savedBook)
            .usingRecursiveComparison()
            .isEqualTo(book2);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        String bookId = "1";
        String authorId = "2";
        String genreId = "3";
        String bookTitle = "BookTitle_Test";

        Author author = mongoTemplate.findById(authorId, Author.class);
        Genre genre = mongoTemplate.findById(genreId, Genre.class);
        Book updateBook = new Book(bookId, bookTitle, author, Collections.singletonList(genre));
        Book savedBook = repository.save(updateBook);
        assertNotNull(savedBook);
        var dbBook = mongoTemplate.findById(savedBook.getId(), Book.class);
        assertThat(savedBook)
            .usingRecursiveComparison()
            .isEqualTo(dbBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        String bookId = "1";
        var optionalBook = repository.findById(bookId);
        var expectedBook = mongoTemplate.findById(bookId, Book.class);
        assertTrue(optionalBook.isPresent());
        assertThat(optionalBook.get())
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Author(Integer.toString(id), "Author-" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(Integer.toString(id), "Genre-" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Book(Integer.toString(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}