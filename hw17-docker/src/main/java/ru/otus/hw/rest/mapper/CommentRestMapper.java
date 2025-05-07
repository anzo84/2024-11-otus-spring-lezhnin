package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.CommentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentRestMapper {

    CommentDto toDto(ru.otus.hw.domain.model.Comment comment);

    List<CommentDto> toDto(List<ru.otus.hw.domain.model.Comment> comments);

    ru.otus.hw.domain.model.Comment map(CommentDto comment);

    List<ru.otus.hw.domain.model.Comment> map(List<CommentDto> comments);
}
