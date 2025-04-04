package ru.otus.hw11.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.rest.model.GenreDto;
import ru.otus.hw11.domain.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreRestMapper {

    GenreDto map(Genre genre);

    Genre map(GenreDto genre);
}
