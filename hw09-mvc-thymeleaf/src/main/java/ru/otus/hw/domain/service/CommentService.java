package ru.otus.hw.domain.service;

import ru.otus.hw.persistence.model.CommentEntity;

import java.util.List;

public interface CommentService {

    List<CommentEntity> findByBookId(long bookId);

    CommentEntity updateComment(long id, String content);

    CommentEntity addComment(String content, long bookId);

    void deleteComment(long id);

    Long count();
}
