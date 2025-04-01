package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentRestMapper {

    CommentDto map(ru.otus.hw.domain.model.Comment comment);

    ru.otus.hw.domain.model.Comment map(CommentDto comment);

}
