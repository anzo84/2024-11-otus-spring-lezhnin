package ru.otus.hw11.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.rest.model.CommentDto;
import ru.otus.hw11.domain.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentRestMapper {

    CommentDto map(Comment comment);

    Comment map(CommentDto comment);

}
