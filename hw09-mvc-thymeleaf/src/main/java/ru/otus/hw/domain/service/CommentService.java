package ru.otus.hw.domain.service;

import ru.otus.hw.domain.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByBookId(long bookId);

    Comment findById(long id);

    Comment save(Comment comment);

    void deleteComment(long id);

    Long count();
}
