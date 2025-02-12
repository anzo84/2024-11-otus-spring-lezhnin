package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(long bookId) {

        return commentRepository.findAllCommentsByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment updateComment(long id, String content) {
        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment addComment(String content, long bookId) {
        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.save(new Comment(0L, content, book));
    }

    @Transactional
    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }
}
