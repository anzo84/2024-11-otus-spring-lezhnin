package ru.otus.hw11.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.domain.model.Genre;
import ru.otus.hw11.persistence.model.GenreEntity;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre map(GenreEntity genreEntity);

    GenreEntity map(Genre genre);
}
