package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.domain.exception.EntityNotFoundException;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.persistence.model.BookEntity;
import ru.otus.hw.persistence.repository.AuthorRepository;
import ru.otus.hw.persistence.repository.BookRepository;
import ru.otus.hw.persistence.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
@Validated
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id).map(bookMapper::map);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookMapper.map(bookRepository.findAll());
    }

    @Transactional
    @Override
    public Book save(@Valid @NotNull(message = "{book.notEmpty}") Book book) throws EntityNotFoundException,
        IllegalArgumentException {
        if (isEmpty(book.getGenres())) {
            throw new IllegalArgumentException("Genres ids must not be empty");
        }
        var author = authorRepository.findById(Optional.ofNullable(book.getAuthor())
                .map(Author::getId).orElseThrow(() -> new IllegalArgumentException("Author must not be empty")))
            .orElseThrow(() -> new EntityNotFoundException(
                "Author with id %d not found",book.getAuthor().getId()));
        BookEntity entity = book.getId() == null ? new BookEntity() :
            bookRepository.findById(book.getId()).orElse(new BookEntity());

        var genresIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(book.getGenres()) || book.getGenres().size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found", genresIds);
        }
        entity.setAuthor(author);
        entity.setTitle(book.getTitle());
        entity.setGenres(genres);
        return bookMapper.map(bookRepository.save(entity));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return bookRepository.count();
    }

}
