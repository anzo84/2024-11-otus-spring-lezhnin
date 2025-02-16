package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.persistence.model.AuthorEntity;
import ru.otus.hw.persistence.model.BookEntity;
import ru.otus.hw.persistence.model.GenreEntity;
import ru.otus.hw.persistence.repository.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий на основе JPA для работы с книгами")
@DataJpaTest
class BookEntityRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository repository;

    private List<AuthorEntity> dbAuthorEntities;

    private List<GenreEntity> dbGenreEntities;

    private List<BookEntity> dbBookEntities;

    @BeforeEach
    void setUp() {
        dbAuthorEntities = getDbAuthors();
        dbGenreEntities = getDbGenres();
        dbBookEntities = getDbBooks(dbAuthorEntities, dbGenreEntities);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(BookEntity expectedBookEntity) {
        var actualBook = repository.findById(expectedBookEntity.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBookEntity);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        var expectedBooks = dbBookEntities;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        long authorId = 1L;
        long genreId = 1L;
        String bookTitle = "BookTitle_Test";
        AuthorEntity authorEntity = testEntityManager.find(AuthorEntity.class, authorId);
        GenreEntity genreEntity = testEntityManager.find(GenreEntity.class, genreId);
        BookEntity newBookEntity = new BookEntity();
        newBookEntity.setAuthor(authorEntity);
        newBookEntity.setGenres(Collections.singletonList(genreEntity));
        newBookEntity.setTitle(bookTitle);
        BookEntity savedBookEntity = repository.save(newBookEntity);
        assertNotNull(savedBookEntity);
        var book2 = testEntityManager.find(BookEntity.class, savedBookEntity.getId());
        assertThat(savedBookEntity)
            .usingRecursiveComparison()
            .isEqualTo(book2);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        long bookId = 1L;
        long authorId = 2L;
        long genreId = 6L;
        String bookTitle = "BookTitle_Test";

        AuthorEntity authorEntity = testEntityManager.find(AuthorEntity.class, authorId);
        GenreEntity genreEntity = testEntityManager.find(GenreEntity.class, genreId);
        BookEntity updateBookEntity = new BookEntity(bookId, bookTitle, authorEntity, Collections.singletonList(genreEntity));
        BookEntity savedBookEntity = repository.save(updateBookEntity);
        assertNotNull(savedBookEntity);
        var dbBook = testEntityManager.find(BookEntity.class, savedBookEntity.getId());
        assertThat(savedBookEntity)
            .usingRecursiveComparison()
            .isEqualTo(dbBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        long bookId = 1L;
        var optionalBook = repository.findById(bookId);
        var expectedBook = testEntityManager.find(BookEntity.class, bookId);
        assertTrue(optionalBook.isPresent());
        assertThat(optionalBook.get())
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    private static List<AuthorEntity> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorEntity(id, "Author_" + id))
                .toList();
    }

    private static List<GenreEntity> getDbGenres() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new GenreEntity(id, "Genre_" + id))
                .toList();
    }

    private static List<BookEntity> getDbBooks(List<AuthorEntity> dbAuthorEntities, List<GenreEntity> dbGenreEntities) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookEntity(Long.valueOf(id),
                        "BookTitle_" + id,
                        dbAuthorEntities.get(id - 1),
                        dbGenreEntities.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<BookEntity> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}