package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.persistence.model.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @EntityGraph(CommentEntity.COMMENT_BOOKS)
    List<CommentEntity> findByBookId(long bookId);
}
