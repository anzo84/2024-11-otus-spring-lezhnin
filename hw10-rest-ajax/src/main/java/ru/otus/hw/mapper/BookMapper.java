package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.persistence.model.BookEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    Book map(BookEntity entity);

    List<Book> map(List<BookEntity> entities);
}
