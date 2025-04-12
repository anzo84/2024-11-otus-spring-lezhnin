package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.rest.api.MetricsApi;
import ru.otus.hw.rest.model.MetricDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MetricsRestController implements MetricsApi {

    private final AuthorService authorService;

    private final BookService bookService;

    private final GenreService genreService;

    private final CommentService commentService;

    @Override
    public ResponseEntity<List<MetricDto>> getMetrics() {
        List<MetricDto> result = new ArrayList<>();
        result.add(new MetricDto(MetricDto.NameEnum.AUTHOR_COUNT, authorService.count()));
        result.add(new MetricDto(MetricDto.NameEnum.BOOK_COUNT, bookService.count()));
        result.add(new MetricDto(MetricDto.NameEnum.GENRE_COUNT, genreService.count()));
        result.add(new MetricDto(MetricDto.NameEnum.COMMENT_COUNT, commentService.count()));
        return ResponseEntity.ok(result);
    }
}
