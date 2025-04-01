package ru.otus.hw.persistence.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.persistence.model.GenreBookEntity;

import java.util.Set;

public interface GenreBookRepository extends ReactiveCrudRepository<GenreBookEntity, Long> {

    Flux<GenreBookEntity> findAllByBookIdIn(Set<Long> bookIds);

    @Modifying
    Mono<Void> deleteAllByBookIdIs(Long bookId);
}
