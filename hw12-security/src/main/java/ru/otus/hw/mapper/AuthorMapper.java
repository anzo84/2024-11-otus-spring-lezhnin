package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.persistence.model.AuthorEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author map(AuthorEntity entity);

    List<Author> map(List<AuthorEntity> entities);
}
