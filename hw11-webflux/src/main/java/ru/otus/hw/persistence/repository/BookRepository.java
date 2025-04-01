package ru.otus.hw.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.persistence.model.BookEntity;

public interface BookRepository extends ReactiveCrudRepository<BookEntity, Long> {
}
