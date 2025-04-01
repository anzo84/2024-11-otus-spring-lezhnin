package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.rest.api.BooksApi;
import ru.otus.hw.rest.mapper.BookRestMapper;
import ru.otus.hw.rest.model.BookDto;
import ru.otus.hw.rest.model.ModifyBookDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookRestController implements BooksApi {

    private final BookService service;

    private final BookRestMapper mapper;

    @Override
    public Mono<BookDto> createBook(Mono<BookDto> bookDto, ServerWebExchange exchange) {
        return service.save(bookDto.map(mapper::map)).map(mapper::map);
    }

    @Override
    public Mono<Void> deleteBook(Long id, ServerWebExchange exchange) {
        return service.delete(id);
    }

    @Override
    public Flux<BookDto> getAllBooks(ServerWebExchange exchange) {
        return service.findAll().map(mapper::map);
    }

    @Override
    public Mono<BookDto> getBookById(Long id, ServerWebExchange exchange) {
        return service.findById(id).map(mapper::map);
    }

    @Override
    public Mono<BookDto> updateBook(Long id, Mono<ModifyBookDto> modifyBookDto, ServerWebExchange exchange) {
        return service.save(modifyBookDto.map(dto -> {
            BookDto bookDto = new BookDto();
            bookDto.setId(id);
            bookDto.setTitle(dto.getTitle());
            bookDto.setAuthor(dto.getAuthor());
            bookDto.setGenres(dto.getGenres());
            return bookDto;
        }).map(mapper::map)).map(mapper::map);
    }
}
