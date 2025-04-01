package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.persistence.repository.BookRepository;
import ru.otus.hw.persistence.repository.CommentRepository;

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
        return commentRepository.findByBookId(bookId).map(mapper::map);
    }

    @Override
    public Mono<Comment> findById(long id) {
        return commentRepository.findById(id).map(mapper::map);
    }

    @Override
    public Mono<Comment> save(@Valid @NotNull(message = "{comment.notEmpty}") Mono<Comment> commentMono) {
        /*  return commentMono.flatMap(comment -> Mono.justOrEmpty(
            Optional.ofNullable(comment.getBook()).map(Book::getId))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Book id is null")))
            .flatMap(bookId -> bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %d not found", bookId))))
            .flatMap(bookEntity -> commentRepository.findById(
                Optional.ofNullable(comment.getId()).orElse(0L))
                .defaultIfEmpty(new CommentEntity())
                .flatMap(commentEntity -> {
                    commentEntity.setBook(bookEntity);
                    commentEntity.setContent(comment.getContent());
                    return commentRepository.save(commentEntity)
                        .map(mapper::map);
                }))).as(transactionalOperator::transactional);*/
        return commentMono;
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
