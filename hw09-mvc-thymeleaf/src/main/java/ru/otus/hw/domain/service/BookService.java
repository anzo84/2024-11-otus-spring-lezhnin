package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.otus.hw.domain.exception.EntityNotFoundException;
import ru.otus.hw.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findById(long id);

    List<Book> findAll();

    Book save(@Valid @NotNull(message = "{book.notEmpty}") Book book) throws EntityNotFoundException,
        IllegalArgumentException;

    void deleteById(long id);

    Long count();
}
