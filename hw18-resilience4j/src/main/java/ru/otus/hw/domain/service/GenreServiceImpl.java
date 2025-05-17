package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.persistence.model.GenreEntity;
import ru.otus.hw.persistence.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Validated
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id).map(genreMapper::map);
    }

    @Transactional(readOnly = true)
    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public List<Genre> findAll() {
        return genreMapper.map(genreRepository.findAll());
    }

    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public Long count() {
        return genreRepository.count();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).AUTHOR)")
    public Genre save(@Valid Genre genre) {
        GenreEntity entity = genre.getId() == null ? new GenreEntity() :
            genreRepository.findById(genre.getId()).orElse(new GenreEntity());
        entity.setName(genre.getName());
        return genreMapper.map(genreRepository.save(entity));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).AUTHOR)")
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole(T(ru.otus.hw.domain.model.Role).COMMENTATOR)")
    public List<Genre> getGenresByIds(List<Long> ids) {
        return genreMapper.map(genreRepository.findAllByIdIn(ids));
    }
}
