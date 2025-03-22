package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.persistence.model.AuthorEntity;
import ru.otus.hw.persistence.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id).map(authorMapper::map);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorMapper.map(authorRepository.findAll());
    }

    @Override
    public Long count() {
        return authorRepository.count();
    }

    @Transactional
    @Override
    public Author save(@Valid Author author) {
        AuthorEntity entity = author.getId() == null ? new AuthorEntity() :
            authorRepository.findById(author.getId()).orElse(new AuthorEntity());
        entity.setFullName(author.getFullName());
        return authorMapper.map(authorRepository.save(entity));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
