package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.GenreDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreRestMapper {

    GenreDto toDto(ru.otus.hw.domain.model.Genre genre);

    List<GenreDto> toDto(List<ru.otus.hw.domain.model.Genre> genres);

    ru.otus.hw.domain.model.Genre map(GenreDto genre);

    List<ru.otus.hw.domain.model.Genre> map(List<GenreDto> genres);
}
