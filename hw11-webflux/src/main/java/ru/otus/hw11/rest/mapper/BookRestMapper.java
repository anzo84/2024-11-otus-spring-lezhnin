package ru.otus.hw11.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw11.rest.model.BookDto;
import ru.otus.hw11.domain.model.Book;

@Mapper(componentModel = "spring")
public interface BookRestMapper {

    BookDto map(Book book);

    Book map(BookDto book);
}
