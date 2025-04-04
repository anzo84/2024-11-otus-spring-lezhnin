package ru.otus.hw11.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.model.Comment;

public interface CommentService {

    Flux<Comment> findByBookId(long bookId);

    Mono<Comment> findById(long id);

    Mono<Comment> save(@Valid @NotNull(message = "{comment.notEmpty}") Mono<Comment> commentMono);

    Mono<Void> delete(long id);

    Mono<Long> count();
}
