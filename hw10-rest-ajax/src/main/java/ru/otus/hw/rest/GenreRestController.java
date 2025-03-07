package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.rest.api.GenresApi;
import ru.otus.hw.rest.mapper.GenreRestMapper;
import ru.otus.hw.rest.model.GenreDto;
import ru.otus.hw.rest.model.UpdateGenreRequestDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GenreRestController implements GenresApi {

    private final GenreService service;

    private final GenreRestMapper mapper;

    @Override
    public ResponseEntity<GenreDto> createGenre(GenreDto genre) {
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(genre))));
    }

    @Override
    public ResponseEntity<Void> deleteGenre(Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(mapper.toDto(service.findAll()));
    }

    @Override
    public ResponseEntity<GenreDto> getGenreById(Long id) {
        Optional<ru.otus.hw.domain.model.Genre> genre = service.findById(Optional.ofNullable(id).orElse(0L));
        return genre.map(value -> ResponseEntity.ok(mapper.toDto(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<GenreDto> updateGenre(Long id, UpdateGenreRequestDto updateGenreRequest) {
        GenreDto genre = new GenreDto();
        genre.setId(id);
        genre.setName(updateGenreRequest.getName());
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(genre))));
    }
}
