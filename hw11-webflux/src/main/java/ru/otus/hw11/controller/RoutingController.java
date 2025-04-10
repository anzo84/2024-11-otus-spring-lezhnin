package ru.otus.hw11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class RoutingController {

    @GetMapping({"/", "/home"})
    public Mono<String> homePage() {
        return Mono.just("home.pug");
    }

    @GetMapping("/genre")
    public Mono<String> genreList() {
        return Mono.just("genre.pug");
    }

    @GetMapping("/author")
    public Mono<String> authorList() {
        return Mono.just("author.pug");
    }

    @GetMapping("/book")
    public Mono<String> bookList() {
        return Mono.just("book.pug");
    }

    @GetMapping("/comment")
    public Mono<String> commentList() {
        return Mono.just("comment.pug");
    }
}
