package ru.otus.hw11.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.model.Author;
import ru.otus.hw11.mapper.AuthorMapper;
import ru.otus.hw11.persistence.model.AuthorEntity;
import ru.otus.hw11.persistence.repository.AuthorRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    private final AuthorMapper mapper;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Author> findById(long id) {
        return repository.findById(id)
            .map(mapper::map);
    }

    @Override
    public Flux<Author> findAll() {
        return repository.findAll().map(mapper::map);
    }

    @Override
    public Mono<Long> count() {
        return repository.count();
    }

    @Override
    public Mono<Author> save(Mono<Author> authorMono) {
        return authorMono.flatMap(author ->
            repository.findById(Optional.ofNullable(author.getId()).orElse(0L))
                .flatMap(existingEntity -> {
                    existingEntity.setFullName(author.getFullName());
                    return repository.save(existingEntity);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    AuthorEntity newEntity = new AuthorEntity();
                    newEntity.setFullName(author.getFullName());
                    return repository.save(newEntity);
                }))
                .map(mapper::map)
        ).as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
