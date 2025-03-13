package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    @GetMapping("home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/")
    public String rootPage() {
        return "redirect:/home";
    }

    @GetMapping("genre")
    public String genreList() {
        return "genre-list";
    }

    @GetMapping("author")
    public String authorList() {
        return "author-list";
    }

    @GetMapping("book")
    public String bookList() {
        return "book-list";
    }

    @GetMapping("comment")
    public String commentList() {
        return "comment-list";
    }
}
