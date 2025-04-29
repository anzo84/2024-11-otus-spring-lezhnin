package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.persistence.model.GenreEntity;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(path = "genres")
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    List<GenreEntity> findAllByIdIn(Collection<Long> ids);
}
