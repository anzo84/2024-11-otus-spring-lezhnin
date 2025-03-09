package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.rest.api.AuthorsApi;
import ru.otus.hw.rest.mapper.AuthorRestMapper;
import ru.otus.hw.rest.model.AuthorDto;
import ru.otus.hw.rest.model.UpdateAuthorRequestDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthorRestController implements AuthorsApi {

    private final AuthorService service;

    private final AuthorRestMapper mapper;

    @Override
    public ResponseEntity<AuthorDto> createAuthor(AuthorDto authorDto) {
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(authorDto))));
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(mapper.toDto(service.findAll()));
    }

    @Override
    public ResponseEntity<AuthorDto> getAuthorById(Long id) {
        Optional<Author> author = service.findById(Optional.ofNullable(id).orElse(0L));
        return author.map(value -> ResponseEntity.ok(mapper.toDto(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AuthorDto> updateAuthor(Long id, UpdateAuthorRequestDto updateAuthorRequestDto) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(id);
        authorDto.setFullName(updateAuthorRequestDto.getFullName());
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(authorDto))));
    }
}
