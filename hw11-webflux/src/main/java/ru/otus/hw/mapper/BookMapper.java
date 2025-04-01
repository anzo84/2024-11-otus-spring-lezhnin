package ru.otus.hw.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.persistence.model.BookEntity;

import java.util.Comparator;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    Book map(BookEntity entity);

    @AfterMapping
    default void sortGenres(BookEntity entity, @MappingTarget Book book) {
        if (book.getGenres() != null) {
            book.getGenres().sort(Comparator.comparing(Genre::getName));
        }
    }
}
