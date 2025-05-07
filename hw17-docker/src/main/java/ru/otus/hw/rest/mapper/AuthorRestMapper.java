package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.AuthorDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorRestMapper {

    AuthorDto toDto(ru.otus.hw.domain.model.Author author);

    List<AuthorDto> toDto(List<ru.otus.hw.domain.model.Author> author);

    ru.otus.hw.domain.model.Author map(AuthorDto authorDto);

    List<ru.otus.hw.domain.model.Author> map(List<AuthorDto> authorDtos);

}
