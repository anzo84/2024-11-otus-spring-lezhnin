package ru.otus.hw.domain.service;

import ru.otus.hw.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> findByBookId(long bookId);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteComment(long id);

    Long count();
}
