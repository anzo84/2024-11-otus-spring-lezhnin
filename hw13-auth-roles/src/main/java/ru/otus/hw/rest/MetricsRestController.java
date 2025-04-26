package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.domain.service.UserService;
import ru.otus.hw.rest.api.MetricsApi;
import ru.otus.hw.rest.model.MetricDto;
import ru.otus.hw.security.SecurityHelper;

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

    private final UserService userService;

    private final SecurityHelper securityHelper;

    @Override
    public ResponseEntity<List<MetricDto>> getMetrics() {
        List<MetricDto> result = new ArrayList<>();
        if (securityHelper.hasRole(Role.ADMINISTRATOR)) {
            result.add(new MetricDto(MetricDto.NameEnum.USER_COUNT, userService.count()));
        }
        if (securityHelper.hasRole(Role.AUTHOR)) {
            result.add(new MetricDto(MetricDto.NameEnum.AUTHOR_COUNT, authorService.count()));
            result.add(new MetricDto(MetricDto.NameEnum.GENRE_COUNT, genreService.count()));
            result.add(new MetricDto(MetricDto.NameEnum.BOOK_COUNT, bookService.count()));
        }
        if (securityHelper.hasRole(Role.COMMENTATOR)) {
            result.add(new MetricDto(MetricDto.NameEnum.COMMENT_COUNT, commentService.count()));
        }
        return ResponseEntity.ok(result);
    }
}
