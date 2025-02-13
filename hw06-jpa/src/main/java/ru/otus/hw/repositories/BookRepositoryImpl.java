package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        return entityManager.createQuery("FROM Book WHERE id = :id", Book.class)
            .setParameter("id", id)
            .setHint(EntityGraph.EntityGraphType.FETCH.getKey(),
                entityManager.getEntityGraph(Book.BOOKS_AUTHOR_GENRES_GRAPH))
            .getResultStream()
            .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("FROM Book", Book.class)
            .setHint(EntityGraph.EntityGraphType.FETCH.getKey(),
                entityManager.getEntityGraph(Book.BOOKS_AUTHOR_GENRES_GRAPH))
            .getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
