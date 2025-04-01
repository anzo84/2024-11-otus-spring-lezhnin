package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.persistence.model.CommentEntity;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface CommentMapper {

    Comment map(CommentEntity entity);

    CommentEntity map(Comment entity);
}
