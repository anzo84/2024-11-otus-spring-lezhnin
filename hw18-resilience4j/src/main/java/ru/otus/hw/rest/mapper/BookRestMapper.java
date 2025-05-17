package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.rest.model.BookDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookRestMapper {

    BookDto toDto(ru.otus.hw.domain.model.Book book);

    List<BookDto> toDto(List<ru.otus.hw.domain.model.Book> books);

    ru.otus.hw.domain.model.Book map(BookDto book);

    List<ru.otus.hw.domain.model.Book> map(List<BookDto> books);
}
