package ru.otus.hw11.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.domain.model.Author;
import ru.otus.hw11.persistence.model.AuthorEntity;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author map(AuthorEntity entity);

    AuthorEntity map(Author author);
}
