package ru.otus.hw11.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw11.persistence.model.BookEntity;

public interface BookRepository extends ReactiveCrudRepository<BookEntity, Long> {
}
