package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations paramJdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        var result = jdbc.query("""
            SELECT b.id AS book_id, 
                   b.title AS book_title, 
                   a.id AS author_id, 
                   a.full_name AS author_full_name,
                   bg.genre_id, 
                   g.name AS genre_name
            FROM books b
            JOIN authors a ON b.author_id = a.id
            LEFT JOIN books_genres bg ON b.id = bg.book_id
            LEFT JOIN genres g ON bg.genre_id = g.id
            WHERE b.id = ?
            """, new BookResultSetExtractor(), id);

        return Optional.ofNullable(result);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("DELETE FROM books WHERE id = ?", id);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("""
                SELECT b.id AS book_id, b.title AS book_title, a.id AS author_id, a.full_name AS author_full_name
                FROM books b
                JOIN authors a ON b.author_id = a.id""",
            new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRelationMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        Map<Long, Book> bookMap = booksWithoutGenres.stream()
            .collect(Collectors.toMap(Book::getId, Function.identity()));
        Map<Long, Genre> genreMap = genres.stream()
            .collect(Collectors.toMap(Genre::getId, Function.identity()));
        relations.forEach(relation -> {
            Book book = bookMap.get(relation.bookId());
            if (book != null) {
                Genre genre = genreMap.get(relation.genreId());
                if (genre != null) {
                    book.getGenres().add(genre);
                }
            }
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        paramJdbc.update("INSERT INTO books (title, author_id) VALUES (:title, :author_id)",
            new MapSqlParameterSource(
                Map.of("title", book.getTitle(),
                    "author_id", book.getAuthor().getId())),
            keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        long countRows = paramJdbc.update("UPDATE books SET title = :title, author_id = :author_id WHERE id = :id",
            Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor().getId()));

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (countRows == 0) {
            throw new EntityNotFoundException(String.format("Book not found (id = %d)", book.getId()));
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        Map<String, Object>[] batchValues = new HashMap[book.getGenres().size()];
        for (int i = 0; i < book.getGenres().size(); i++) {
            batchValues[i] = new HashMap<>();
            batchValues[i].put("book_id", book.getId());
            batchValues[i].put("genre_id", book.getGenres().get(i).getId());
        }
        paramJdbc.batchUpdate("INSERT INTO books_genres (book_id, genre_id) VALUES (:book_id, :genre_id)", batchValues);
    }

    private void removeGenresRelationsFor(Book book) {
        paramJdbc.update("DELETE FROM books_genres WHERE book_id = :book_id",
            Map.of("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
            Book book = new Book(rs.getLong("book_id"), rs.getString("book_title"), author, new ArrayList<>());
            return book;
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.isBeforeFirst()) {
                return null;
            }

            Book book = new Book();
            List<Genre> genres = new ArrayList<>();
            book.setGenres(genres);
            while (rs.next()) {
                if (book.getId() == 0) {
                    book.setId(rs.getLong("book_id"));
                    book.setTitle(rs.getString("book_title"));
                    Author author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
                    book.setAuthor(author);
                }
                book.getGenres().add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return book;
        }
    }

    private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
