package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.domain.service.GenreService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final GenreService genreService;

    private final AuthorService authorService;

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping(path = {"/", "/home"})
    public String home(Model model) {
        model.addAttribute("genreCount", genreService.count());
        model.addAttribute("authorCount", authorService.count());
        model.addAttribute("commentCount", commentService.count());
        model.addAttribute("bookCount", bookService.count());

        return "home";
    }
}
