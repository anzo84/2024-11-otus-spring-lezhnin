package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.persistence.model.GenreEntity;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    List<GenreEntity> findAllByIdIn(Collection<Long> ids);
}
