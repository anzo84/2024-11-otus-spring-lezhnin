package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RoutingController {

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
        return "genre";
    }

    @GetMapping("author")
    public String authorList() {
        return "author";
    }

    @GetMapping("book")
    public String bookList() {
        return "book";
    }

    @GetMapping("comment")
    public String commentList() {
        return "comment";
    }
}
