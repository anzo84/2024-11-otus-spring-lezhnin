package ru.otus.hw.controller.formatter;

import org.springframework.format.Formatter;
import ru.otus.hw.domain.model.Genre;

import java.text.ParseException;

import java.util.Locale;

public class GenreFormatter implements Formatter<Genre> {
    @Override
    public String print(Genre genre, Locale locale) {
        return genre.getId().toString();
    }

    @Override
    public Genre parse(String id, Locale locale) throws ParseException {
        Genre genre = new Genre();
        try {
            genre.setId(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ParseException(e.getMessage(), 0);
        }
        return genre;
    }
}
