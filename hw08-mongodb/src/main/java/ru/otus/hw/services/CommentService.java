package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByBookId(long bookId);

    Comment updateComment(long id, String content);

    Comment addComment(String content, long bookId);

    void deleteComment(long id);
}
