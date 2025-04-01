package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.BookDto;

@Mapper(componentModel = "spring")
public interface BookRestMapper {

    BookDto map(ru.otus.hw.domain.model.Book book);

    ru.otus.hw.domain.model.Book map(BookDto book);
}
