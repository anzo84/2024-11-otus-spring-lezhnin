package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.persistence.model.GenreEntity;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre map(GenreEntity genreEntity);

    GenreEntity map(Genre genre);
}
