package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GenreController {

    public static final Map<String, String> GENRE_LIST_BREADCRUMBS = Map.of("genre.list", "/genre");

    @GetMapping("genre")
    public String genreList() {
        return "genre-list";
    }

}
