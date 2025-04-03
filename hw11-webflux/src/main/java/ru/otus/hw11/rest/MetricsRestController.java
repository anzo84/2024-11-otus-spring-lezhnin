package ru.otus.hw11.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.service.AuthorService;
import ru.otus.hw11.domain.service.BookService;
import ru.otus.hw11.domain.service.CommentService;
import ru.otus.hw11.domain.service.GenreService;
import ru.otus.hw11.rest.api.MetricsApi;
import ru.otus.hw11.rest.model.MetricDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MetricsRestController implements MetricsApi {

    private final AuthorService authorService;

    private final BookService bookService;

    private final GenreService genreService;

    private final CommentService commentService;

    @Override
    public Flux<MetricDto> getMetrics(ServerWebExchange exchange) {
        Mono<MetricDto> genreCount = genreService.count()
            .map(x -> new MetricDto(MetricDto.NameEnum.GENRE_COUNT, x));
        Mono<MetricDto> authorCount = authorService.count()
            .map(x -> new MetricDto(MetricDto.NameEnum.AUTHOR_COUNT, x));
        Mono<MetricDto> bookCount = bookService.count()
            .map(x -> new MetricDto(MetricDto.NameEnum.BOOK_COUNT, x));
        Mono<MetricDto> commentCount = commentService.count()
            .map(x -> new MetricDto(MetricDto.NameEnum.COMMENT_COUNT, x));

        return Flux.merge(
            genreCount, authorCount, bookCount, commentCount
        );
    }
}
