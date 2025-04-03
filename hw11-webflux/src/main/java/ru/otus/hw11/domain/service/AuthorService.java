package ru.otus.hw11.domain.service;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.model.Author;


public interface AuthorService {

    Mono<Author> findById(long id);

    Flux<Author> findAll();

    Mono<Long> count();

    Mono<Author> save(@Valid Mono<Author> authorMono);

    Mono<Void> delete(Long id);
}
