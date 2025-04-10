package ru.otus.hw11.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.service.AuthorService;
import ru.otus.hw11.rest.api.AuthorsApi;
import ru.otus.hw11.rest.mapper.AuthorRestMapper;
import ru.otus.hw11.rest.model.AuthorDto;
import ru.otus.hw11.rest.model.ModifyAuthorDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthorRestController implements AuthorsApi {

    private final AuthorService service;

    private final AuthorRestMapper mapper;

    @Override
    public Mono<AuthorDto> createAuthor(Mono<AuthorDto> authorDto, ServerWebExchange exchange) {
        return service.save(authorDto.map(mapper::map)).map(mapper::map);
    }

    @Override
    public Mono<Void> deleteAuthor(Long id, ServerWebExchange exchange) {
        return service.delete(id);
    }

    @Override
    public Flux<AuthorDto> getAllAuthors(ServerWebExchange exchange) {
        return service.findAll().map(mapper::map);
    }

    @Override
    public Mono<AuthorDto> getAuthorById(Long id, ServerWebExchange exchange) {
        return service.findById(id).map(mapper::map);
    }

    @Override
    public Mono<AuthorDto> updateAuthor(Long id, Mono<ModifyAuthorDto> modifyAuthorDto, ServerWebExchange exchange) {
        return service.save(modifyAuthorDto.map(dto -> {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(id);
            authorDto.setFullName(dto.getFullName());
            return authorDto;
        }).map(mapper::map)).map(mapper::map);
    }
}
