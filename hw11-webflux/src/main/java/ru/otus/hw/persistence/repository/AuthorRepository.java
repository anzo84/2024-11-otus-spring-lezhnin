package ru.otus.hw.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.persistence.model.AuthorEntity;

public interface AuthorRepository extends ReactiveCrudRepository<AuthorEntity, Long> {
}
