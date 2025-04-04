package ru.otus.hw11.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw11.persistence.model.AuthorEntity;

public interface AuthorRepository extends ReactiveCrudRepository<AuthorEntity, Long> {
}
