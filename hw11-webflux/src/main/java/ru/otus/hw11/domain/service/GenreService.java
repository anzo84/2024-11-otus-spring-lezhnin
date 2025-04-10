package ru.otus.hw11.domain.service;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.model.Genre;

import java.util.List;

public interface GenreService {

    Mono<Genre> findById(long id);

    Flux<Genre> findAll();

    Mono<Long> count();

    Mono<Genre> save(@Valid Mono<Genre> genreMono);

    Mono<Void> delete(Long id);

    Flux<Genre> getGenresByIds(List<Long> ids);
}
