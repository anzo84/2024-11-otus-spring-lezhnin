package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(long bookId) {
        return commentMapper.map(commentRepository.findByBookId(bookId));
    }

    @Override
    public Comment findById(long id) {
        return commentMapper.map(commentRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Comment with id %d not found", id)));
    }

    @Transactional
    @Override
    public Comment save(@Valid @NotNull(message = "{comment.notEmpty}") Comment comment) {
        long bookId = Optional.ofNullable(comment.getBook()).map(BookEntity::getId)
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
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return commentRepository.count();
    }
}
