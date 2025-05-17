package ru.otus.hw.domain.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.domain.exception.EntityNotFoundException;
import ru.otus.hw.domain.model.Book;
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
@RateLimiter(name = "defaultRateLimiter")
@CircuitBreaker(name = "defaultCircuitBreaker")
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public List<Comment> findByBookId(long bookId) {
        return commentMapper.map(commentRepository.findByBookId(bookId));
    }

    @Transactional(readOnly = true)
    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id).map(commentMapper::map);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public Comment save(@Valid @NotNull(message = "{comment.notEmpty}") Comment comment) {
        long bookId = Optional.ofNullable(comment.getBook()).map(Book::getId)
            .orElseThrow(() -> new IllegalArgumentException("Book id is null"));
        BookEntity book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found", bookId));
        var commentEntity = commentRepository.findById(Optional.ofNullable(comment.getId()).orElse(0L))
            .orElse(new CommentEntity());
        commentEntity.setBook(book);
        commentEntity.setContent(comment.getContent());
        return commentMapper.map(commentRepository.save(commentEntity));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).AUTHOR)")
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public Long count() {
        return commentRepository.count();
    }
}
