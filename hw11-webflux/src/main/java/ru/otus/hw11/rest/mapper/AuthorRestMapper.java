package ru.otus.hw11.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.rest.model.AuthorDto;
import ru.otus.hw11.domain.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorRestMapper {

    AuthorDto map(Author author);

    Author map(AuthorDto authorDto);

}
