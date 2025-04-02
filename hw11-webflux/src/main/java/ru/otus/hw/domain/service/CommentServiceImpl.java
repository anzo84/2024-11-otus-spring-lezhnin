package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.exception.EntityNotFoundException;
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.persistence.model.BookEntity;
import ru.otus.hw.persistence.model.CommentEntity;
import ru.otus.hw.persistence.repository.BookRepository;
import ru.otus.hw.persistence.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper mapper;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Flux<Comment> findByBookId(long bookId) {
        return Mono.zip(
                commentRepository.findByBookId(bookId).collectList(),
                bookRepository.findById(bookId)
            )
            .flatMapMany(tuple -> {
                List<CommentEntity> comments = tuple.getT1();
                BookEntity bookEntity = tuple.getT2();
                comments.forEach(comment -> comment.setBook(bookEntity));
                return Flux.fromIterable(comments).map(mapper::map);
            }).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Comment> findById(long id) {
        return commentRepository.findById(id)
            .flatMap(commentEntity ->
                bookRepository.findById(commentEntity.getBookId())
                    .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        String.format("Book with id %d not found", commentEntity.getBookId()))))
                    .map(bookEntity -> {
                        commentEntity.setBook(bookEntity);
                        return commentEntity;
                    }))
            .map(mapper::map)
            .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Comment> save(@Valid @NotNull(message = "{comment.notEmpty}") Mono<Comment> commentMono) {
        return commentMono
            .flatMap(comment -> {
                if (comment.getBook() == null || comment.getBook().getId() == null) {
                    return Mono.error(new IllegalArgumentException("Book must be specified"));
                }
                Long bookId = comment.getBook().getId();
                Long commentId = Optional.ofNullable(comment.getId()).orElse(0L);
                return bookRepository.findById(bookId)
                    .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        String.format("Book with id %d not found", bookId))))
                    .flatMap(bookEntity -> commentRepository.findById(commentId)
                        .defaultIfEmpty(new CommentEntity())
                        .flatMap(commentEntity -> {
                            commentEntity.setContent(comment.getContent());
                            commentEntity.setBookId(bookId);
                            return commentRepository.save(commentEntity)
                                .map(savedEntity -> {
                                    Comment mapped = mapper.map(savedEntity);
                                    mapped.setBook(comment.getBook());
                                    return mapped;
                                });
                        }));
            }).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> delete(long id) {
        return commentRepository.deleteById(id);
    }

    @Override
    public Mono<Long> count() {
        return commentRepository.count();
    }

}
