package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.GenreDto;

@Mapper(componentModel = "spring")
public interface GenreRestMapper {

    GenreDto map(ru.otus.hw.domain.model.Genre genre);

    ru.otus.hw.domain.model.Genre map(GenreDto genre);
}
