package ru.otus.hw11.domain.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.model.Genre;
import ru.otus.hw11.mapper.GenreMapper;
import ru.otus.hw11.persistence.model.GenreEntity;
import ru.otus.hw11.persistence.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    private final GenreMapper mapper;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Genre> findById(long id) {
        return repository.findById(id).map(mapper::map);
    }

    @Override
    public Flux<Genre> findAll() {
        return repository.findAll().map(mapper::map);
    }

    @Override
    public Mono<Long> count() {
        return repository.count();
    }

    @Override
    public Mono<Genre> save(@Valid Mono<Genre> genreMono) {
        return genreMono.flatMap(genre ->
            repository.findById(Optional.ofNullable(genre.getId()).orElse(0L))
                .flatMap(existingEntity -> {
                    existingEntity.setName(genre.getName());
                    return repository.save(existingEntity);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    GenreEntity newEntity = new GenreEntity();
                    newEntity.setName(genre.getName());
                    return repository.save(newEntity);
                }))
                .map(mapper::map)
        )
            .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Genre> getGenresByIds(List<Long> ids) {
        return repository.findAllByIdIn(ids).map(mapper::map);
    }

}
