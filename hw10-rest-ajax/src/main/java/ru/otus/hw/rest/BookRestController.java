package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.rest.api.BooksApi;
import ru.otus.hw.rest.mapper.BookRestMapper;
import ru.otus.hw.rest.model.BookDto;
import ru.otus.hw.rest.model.ModifyBookDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookRestController implements BooksApi {

    private final BookService service;

    private final BookRestMapper mapper;

    @Override
    public ResponseEntity<BookDto> createBook(BookDto bookDto) {
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(bookDto))));
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(mapper.toDto(service.findAll()));
    }

    @Override
    public ResponseEntity<BookDto> getBookById(Long id) {
        Optional<Book> book = service.findById(Optional.ofNullable(id).orElse(0L));
        return book.map(value -> ResponseEntity.ok(mapper.toDto(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<BookDto> updateBook(Long id, ModifyBookDto updateBookRequestDto) {
        BookDto book = new BookDto();
        book.setId(id);
        book.setTitle(updateBookRequestDto.getTitle());
        book.setAuthor(updateBookRequestDto.getAuthor());
        book.setGenres(updateBookRequestDto.getGenres());
        return  ResponseEntity.ok(mapper.toDto(service.save(mapper.map(book))));
    }
}
