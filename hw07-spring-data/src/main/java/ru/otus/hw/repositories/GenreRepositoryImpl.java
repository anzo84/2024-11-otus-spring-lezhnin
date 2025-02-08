package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        return entityManager.createQuery("FROM Genre", Genre.class).getResultList();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        return entityManager.createQuery("FROM Genre WHERE id IN (:ids)", Genre.class)
            .setParameter("ids", ids)
            .getResultList();
    }

}
