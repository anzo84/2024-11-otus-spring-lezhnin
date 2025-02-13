package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        return entityManager.createQuery("FROM Comment WHERE id = :id", Comment.class)
            .setParameter("id", id)
            .setHint(EntityGraph.EntityGraphType.FETCH.getKey(),
                entityManager.getEntityGraph(Comment.COMMENT_BOOKS))
            .getResultStream()
            .findFirst();
    }

    @Override
    public List<Comment> findAllCommentsByBookId(long bookId) {
        return entityManager.createQuery("FROM Comment cm WHERE cm.book.id = :bookId", Comment.class)
            .setParameter("bookId", bookId)
            .setHint(EntityGraph.EntityGraphType.FETCH.getKey(),
                entityManager.getEntityGraph(Comment.COMMENT_BOOKS))
            .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(long commentId) {
        findById(commentId).ifPresent(entityManager::remove);
    }
}
