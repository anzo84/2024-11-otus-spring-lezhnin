package ru.otus.hw11.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.domain.model.Comment;
import ru.otus.hw11.persistence.model.CommentEntity;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface CommentMapper {

    Comment map(CommentEntity entity);

    CommentEntity map(Comment entity);
}
