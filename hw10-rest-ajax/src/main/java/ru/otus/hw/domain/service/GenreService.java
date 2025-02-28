package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import ru.otus.hw.domain.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    Optional<Genre> findById(long id);

    List<Genre> findAll();

    Long count();

    Genre save(@Valid Genre genre);

    void delete(Long id);
}
