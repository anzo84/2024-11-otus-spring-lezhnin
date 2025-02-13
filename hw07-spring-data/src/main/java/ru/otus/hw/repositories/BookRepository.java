package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(Book.BOOKS_AUTHOR_GENRES_GRAPH)
    Optional<Book> findById(long id);

    @Override
    @EntityGraph(Book.BOOKS_AUTHOR_GENRES_GRAPH)
    List<Book> findAll();
}
