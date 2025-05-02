package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.persistence.model.AuthorEntity;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
