package ru.otus.hw11.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.persistence.model.CommentEntity;


public interface CommentRepository extends R2dbcRepository<CommentEntity, Long> {

    Flux<CommentEntity> findByBookId(long bookId);

    @Override
    Mono<CommentEntity> findById(Long id);
}
