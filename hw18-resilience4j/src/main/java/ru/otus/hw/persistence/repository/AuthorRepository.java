package ru.otus.hw.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.persistence.model.AuthorEntity;

@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
