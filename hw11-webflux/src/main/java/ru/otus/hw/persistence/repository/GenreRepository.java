package ru.otus.hw.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.persistence.model.GenreEntity;

import java.util.Collection;

public interface GenreRepository extends ReactiveCrudRepository<GenreEntity, Long> {

    Flux<GenreEntity> findAllByIdIn(Collection<Long> ids);
}
