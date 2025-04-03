package ru.otus.hw11.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.service.GenreService;
import ru.otus.hw11.rest.api.GenresApi;
import ru.otus.hw11.rest.mapper.GenreRestMapper;
import ru.otus.hw11.rest.model.GenreDto;
import ru.otus.hw11.rest.model.ModifyGenreDto;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GenreRestController implements GenresApi {

    private final GenreService service;

    private final GenreRestMapper mapper;

    @Override
    public Mono<GenreDto> createGenre(Mono<GenreDto> genreDto, ServerWebExchange exchange) {
        return service.save(genreDto.map(mapper::map)).map(mapper::map);
    }

    @Override
    public Mono<Void> deleteGenre(Long id, ServerWebExchange exchange) {
        return service.delete(id);
    }

    @Override
    public Flux<GenreDto> getAllGenres(ServerWebExchange exchange) {
        return service.findAll().map(mapper::map);
    }

    @Override
    public Mono<GenreDto> getGenreById(Long id, ServerWebExchange exchange) {
        return service.findById(id).map(mapper::map);
    }

    @Override
    public Flux<GenreDto> getGenresByIds(List<Long> ids, ServerWebExchange exchange) {
        return service.getGenresByIds(ids).map(mapper::map);
    }

    @Override
    public Mono<GenreDto> updateGenre(Long id, Mono<ModifyGenreDto> modifyGenreDto, ServerWebExchange exchange) {
        return service.save(modifyGenreDto.map(dto -> {
            GenreDto genre = new GenreDto();
            genre.setId(id);
            genre.setName(dto.getName());
            return genre;
        }).map(mapper::map)).map(mapper::map);
    }
}
