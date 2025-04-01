package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.persistence.model.AuthorEntity;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author map(AuthorEntity entity);

    AuthorEntity map(Author author);
}
