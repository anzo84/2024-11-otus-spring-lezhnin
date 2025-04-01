package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.AuthorDto;

@Mapper(componentModel = "spring")
public interface AuthorRestMapper {

    AuthorDto map(ru.otus.hw.domain.model.Author author);

    ru.otus.hw.domain.model.Author map(AuthorDto authorDto);

}
