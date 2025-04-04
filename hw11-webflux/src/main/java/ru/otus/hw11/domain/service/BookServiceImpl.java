package ru.otus.hw11.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.exception.EntityNotFoundException;
import ru.otus.hw11.domain.model.Book;
import ru.otus.hw11.domain.model.Genre;
import ru.otus.hw11.mapper.BookMapper;
import ru.otus.hw11.persistence.model.AuthorEntity;
import ru.otus.hw11.persistence.model.BookEntity;
import ru.otus.hw11.persistence.model.GenreBookEntity;
import ru.otus.hw11.persistence.model.GenreEntity;
import ru.otus.hw11.persistence.repository.AuthorRepository;
import ru.otus.hw11.persistence.repository.BookRepository;
import ru.otus.hw11.persistence.repository.GenreBookRepository;
import ru.otus.hw11.persistence.repository.GenreRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final GenreBookRepository genreBookRepository;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Book> findById(long id) {
        return bookRepository.findById(id)
            .flatMap(book -> {
                Mono<AuthorEntity> authorMono = authorRepository.findById(book.getAuthorId());
                Mono<Set<GenreEntity>> genresMono = loadGenresForBook(id).collect(Collectors.toSet());

                return Mono.zip(authorMono, genresMono)
                    .map(tuple -> {
                        book.setAuthor(tuple.getT1());
                        book.setGenres(tuple.getT2());
                        return book;
                    });
            })
            .map(mapper::map);
    }

    private Flux<GenreEntity> loadGenresForBook(Long bookId) {
        Flux<Long> genreIds = genreBookRepository.findAllByBookIdIn(Set.of(bookId))
            .map(GenreBookEntity::getGenreId);
        return genreIds.collectList()
            .flatMapMany(genreRepository::findAllByIdIn);
    }


    @Override
    public Mono<Book> save(Mono<Book> bookMono) {
        return bookMono
            .flatMap(nonValidateBook -> validateBook(nonValidateBook)
                .flatMap(book -> {
                    Set<Long> genreIds = extractGenreIds(book);
                    return Mono.zip(
                            fetchAuthor(book.getAuthor().getId()),
                            fetchGenres(genreIds),
                            fetchOrCreateBookEntity(book.getId())
                        )
                        .flatMap(tuple -> {
                            AuthorEntity author = tuple.getT1();
                            Set<GenreEntity> genres = tuple.getT2();
                            BookEntity bookEntity = tuple.getT3();
                            updateBookEntity(bookEntity, book.getTitle(), author.getId(), genres);
                            return saveBookWithRelations(bookEntity, genres, book.getId())
                                .map(BookEntity::getId)
                                .flatMap(this::findById);
                        });
                }))
                .as(transactionalOperator::transactional);
    }

    /**
     * Выполняет валидацию данных книги.
     *
     * @param book книга для валидации
     * @return Mono с валидированной книгой или ошибку
     */
    private Mono<Book> validateBook(Book book) {
        return Mono.defer(() -> {
            if (book.getTitle() == null || book.getTitle().isBlank()) {
                return Mono.error(new IllegalArgumentException("Book title must not be empty"));
            }
            if (book.getAuthor() == null || book.getAuthor().getId() == null) {
                return Mono.error(new IllegalArgumentException("Author must be specified"));
            }
            if (book.getGenres() == null || book.getGenres().isEmpty()) {
                return Mono.error(new IllegalArgumentException("At least one genre must be specified"));
            }
            return Mono.just(book);
        });
    }

    /**
     * Извлекает идентификаторы жанров из книги.
     *
     * @param book книга
     * @return множество идентификаторов жанров
     */
    private Set<Long> extractGenreIds(Book book) {
        return book.getGenres().stream()
            .map(Genre::getId)
            .collect(Collectors.toSet());
    }

    /**
     * Загружает автора по идентификатору.
     *
     * @param authorId идентификатор автора
     * @return Mono с автором
     * @throws EntityNotFoundException если автор не найден
     */
    private Mono<AuthorEntity> fetchAuthor(Long authorId) {
        return authorRepository.findById(authorId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException(
                "Author not found with id: " + authorId)));
    }

    /**
     * Загружает жанры по идентификаторам.
     *
     * @param genreIds множество идентификаторов жанров
     * @return Mono с множеством жанров
     * @throws EntityNotFoundException если какие-то жанры не найдены
     */
    private Mono<Set<GenreEntity>> fetchGenres(Set<Long> genreIds) {
        return genreRepository.findAllByIdIn(genreIds)
            .collectList()
            .flatMap(foundGenres -> {
                Set<Long> foundIds = foundGenres.stream()
                    .map(GenreEntity::getId)
                    .collect(Collectors.toSet());

                if (foundIds.size() != genreIds.size()) {
                    Set<Long> missingIds = genreIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());
                    return Mono.error(new EntityNotFoundException(
                        "Genres not found with ids: " + missingIds));
                }
                return Mono.just(new HashSet<>(foundGenres));
            });
    }

    /**
     * Загружает существующую книгу или создает новую.
     *
     * @param bookId идентификатор книги (может быть null для новой книги)
     * @return Mono с сущностью книги
     */
    private Mono<BookEntity> fetchOrCreateBookEntity(Long bookId) {
        return bookId != null
            ? bookRepository.findById(bookId).defaultIfEmpty(new BookEntity())
            : Mono.just(new BookEntity());
    }

    /**
     * Обновляет данные сущности книги.
     *
     * @param bookEntity сущность книги для обновления
     * @param title новый заголовок
     * @param authorId новый идентификатор автора
     * @param genres новое множество жанров
     */
    private void updateBookEntity(BookEntity bookEntity, String title, Long authorId, Set<GenreEntity> genres) {
        bookEntity.setTitle(title);
        bookEntity.setAuthorId(authorId);
        bookEntity.setGenres(genres);
    }

    /**
     * Сохраняет книгу и управляет связями с жанрами.
     *
     * @param bookEntity сущность книги
     * @param genres множество жанров
     * @param originalBookId оригинальный идентификатор книги (null для новой книги)
     * @return Mono с сохраненной сущностью книги
     */
    private Mono<BookEntity> saveBookWithRelations(BookEntity bookEntity, Set<GenreEntity> genres, Long originalBookId) {
        return bookRepository.save(bookEntity)
            .flatMap(savedBook -> {
                Mono<Void> deleteOperation = originalBookId != null
                    ? genreBookRepository.deleteAllByBookIdIs(savedBook.getId())
                    : Mono.empty();
                return deleteOperation
                    .then(createGenreBookRelations(savedBook.getId(), genres))
                    .thenReturn(savedBook);
            });
    }

    /**
     * Создает связи между книгой и жанрами.
     *
     * @param bookId идентификатор книги
     * @param genres множество жанров
     * @return Mono, завершающийся после создания всех связей
     */
    private Mono<Void> createGenreBookRelations(Long bookId, Set<GenreEntity> genres) {
        return Flux.fromIterable(genres)
            .flatMap(genre -> {
                GenreBookEntity gbe = new GenreBookEntity();
                gbe.setBookId(bookId);
                gbe.setGenreId(genre.getId());
                return genreBookRepository.save(gbe);
            })
            .then();
    }

    @Override
    public Mono<Void> delete(long id) {
        return genreBookRepository.deleteAllByBookIdIs(id)
            .then(bookRepository.deleteById(id))
            .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Long> count() {
        return bookRepository.count();
    }

    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll().collectList()
            .flatMapMany(books -> {
                if (books.isEmpty()) {
                    return Flux.empty();
                }

                return fetchAuthorsForBooks(books)
                    .zipWith(fetchGenresForBooks(books))
                    .flatMapMany(tuple -> assembleBooksWithAuthorsAndGenres(
                        books, tuple.getT1(), tuple.getT2()
                    ));
            });
    }

    private Mono<Map<Long, AuthorEntity>> fetchAuthorsForBooks(List<BookEntity> books) {
        Set<Long> authorIds = extractAuthorIds(books);
        return authorRepository.findAllById(authorIds)
            .collectMap(AuthorEntity::getId);
    }

    private Mono<BooksGenresData> fetchGenresForBooks(List<BookEntity> books) {
        Set<Long> bookIds = extractBookIds(books);

        return genreBookRepository.findAllByBookIdIn(bookIds)
            .collectMultimap(GenreBookEntity::getBookId)
            .flatMap(this::fetchGenresForBookRelations);
    }

    private Mono<BooksGenresData> fetchGenresForBookRelations(Map<Long, Collection<GenreBookEntity>> genreBookMap) {
        Set<Long> genreIds = extractGenreIdsFromRelations(genreBookMap);

        return genreRepository.findAllByIdIn(genreIds)
            .collectMap(GenreEntity::getId)
            .map(genreMap -> new BooksGenresData(genreBookMap, genreMap));
    }

    private Flux<Book> assembleBooksWithAuthorsAndGenres(
        List<BookEntity> books,
        Map<Long, AuthorEntity> authorsMap,
        BooksGenresData genresData
    ) {
        return Flux.fromIterable(books)
            .map(book -> enrichBookWithAuthorAndGenres(book, authorsMap, genresData))
            .map(mapper::map);
    }

    private BookEntity enrichBookWithAuthorAndGenres(
        BookEntity book,
        Map<Long, AuthorEntity> authorsMap,
        BooksGenresData genresData
    ) {
        book.setAuthor(authorsMap.get(book.getAuthorId()));
        book.setGenres(getGenresForBook(book.getId(), genresData));
        return book;
    }

    private Set<GenreEntity> getGenresForBook(Long bookId, BooksGenresData genresData) {
        Collection<GenreBookEntity> genreBooks = genresData.genreBookMap().get(bookId);
        if (genreBooks == null) {
            return Collections.emptySet();
        }

        return genreBooks.stream()
            .map(gb -> genresData.genreMap().get(gb.getGenreId()))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    // Вспомогательные методы извлечения ID
    private Set<Long> extractAuthorIds(List<BookEntity> books) {
        return books.stream()
            .map(BookEntity::getAuthorId)
            .collect(Collectors.toSet());
    }

    private Set<Long> extractBookIds(List<BookEntity> books) {
        return books.stream()
            .map(BookEntity::getId)
            .collect(Collectors.toSet());
    }

    private Set<Long> extractGenreIdsFromRelations(Map<Long, Collection<GenreBookEntity>> genreBookMap) {
        return genreBookMap.values().stream()
            .flatMap(Collection::stream)
            .map(GenreBookEntity::getGenreId)
            .collect(Collectors.toSet());
    }

    // Вспомогательный record для хранения данных о жанрах
    private record BooksGenresData(
        Map<Long, Collection<GenreBookEntity>> genreBookMap,
        Map<Long, GenreEntity> genreMap
    ) {}
}
