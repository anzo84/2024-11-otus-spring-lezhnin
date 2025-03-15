package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.otus.hw.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> findByBookId(long bookId);

    Optional<Comment> findById(long id);

    Comment save(@Valid @NotNull(message = "{comment.notEmpty}") Comment comment);

    void deleteComment(long id);

    Long count();
}
