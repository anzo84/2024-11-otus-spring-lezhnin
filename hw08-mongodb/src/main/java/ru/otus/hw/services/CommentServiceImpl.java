package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    public List<Comment> findByBookId(String bookId) {
        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return commentRepository.findByBook(book);
    }

    @Override
    public Comment updateComment(String id, String content) {
        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public Comment addComment(String content, String bookId) {
        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return commentRepository.save(Comment.builder().book(book).content(content).build());
    }

    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
