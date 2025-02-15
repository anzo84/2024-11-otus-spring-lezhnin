package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByBookId(String bookId);

    Comment updateComment(String id, String content);

    Comment addComment(String content, String bookId);

    void deleteComment(String id);
}
