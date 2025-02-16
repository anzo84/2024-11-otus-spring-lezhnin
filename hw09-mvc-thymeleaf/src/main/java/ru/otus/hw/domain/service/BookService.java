package ru.otus.hw.domain.service;

import ru.otus.hw.persistence.model.BookEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookEntity> findById(long id);

    List<BookEntity> findAll();

    BookEntity insert(String title, long authorId, Set<Long> genresIds);

    BookEntity update(long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);

    Long count();
}
