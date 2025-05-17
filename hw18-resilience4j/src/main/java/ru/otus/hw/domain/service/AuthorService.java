package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import ru.otus.hw.domain.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Optional<Author> findById(long id);

    List<Author> findAll();

    Long count();

    Author save(@Valid Author author);

    void delete(Long id);
}
