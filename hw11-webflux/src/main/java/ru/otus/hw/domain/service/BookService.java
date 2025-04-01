package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.model.Book;

public interface BookService {

    Mono<Book> findById(long id);

    /**
     * Возвращает все книги с полной информацией об авторах и жанрах.
     * Жанры для каждой книги сортируются по названию.
     *
     * @return Flux всех книг
     */
    Flux<Book> findAll();

    /**
     * Сохраняет книгу (создает новую или обновляет существующую).
     * Включает валидацию данных и управление связями с автором и жанрами.
     *
     * @param bookMono Mono с данными книги для сохранения
     * @return Mono с сохраненной книгой
     * @throws IllegalArgumentException если данные книги невалидны
     */
    Mono<Book> save(@Valid @NotNull(message = "{book.notEmpty}") Mono<Book> bookMono);

    /**
     * Удаляет книгу по идентификатору.
     * Автоматически удаляет все связанные с книгой жанры (cascade delete).
     *
     * @param id идентификатор книги
     * @return Mono, завершающийся после удаления
     */
    Mono<Void> delete(long id);

    /**
     * Возвращает общее количество книг.
     *
     * @return Mono с количеством книг
     */
    Mono<Long> count();

}
