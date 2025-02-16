package ru.otus.hw.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.exception.EntityNotFoundException;
import ru.otus.hw.persistence.model.CommentEntity;
import ru.otus.hw.persistence.repository.BookRepository;
import ru.otus.hw.persistence.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentEntity> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public CommentEntity updateComment(long id, String content) {
        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public CommentEntity addComment(String content, long bookId) {
        var book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.save(new CommentEntity(0L, content, book));
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
