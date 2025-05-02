package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.persistence.model.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @EntityGraph(BookEntity.BOOKS_AUTHOR_GENRES_GRAPH)
    Optional<BookEntity> findById(long id);

    @Override
    @EntityGraph(BookEntity.BOOKS_AUTHOR_GENRES_GRAPH)
    List<BookEntity> findAll();
}
