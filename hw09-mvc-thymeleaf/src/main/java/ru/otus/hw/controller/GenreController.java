package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.GenreService;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class GenreController {

    public static final Map<String, String> GENRE_LIST_BREADCRUMBS = Map.of("genre.list", "genre");

    private final GenreService genreService;

    @GetMapping("genre")
    public String genreList(Model model) {
        model.addAttribute("genres", genreService.findAll());
        return "genre-list";
    }

    @GetMapping("genre/save")
    public String saveGenre(@RequestParam Optional<Long> id, Model model) {
        Genre genre = id.map(genreService::findById).flatMap(x -> x).orElse(new Genre());
        model.addAttribute("genre", genre);
        model.addAttribute("breadcrumbs", GENRE_LIST_BREADCRUMBS);
        return "genre-save";
    }

    @PostMapping("genre/save")
    public String saveGenre(Model model, @Valid Genre genre, BindingResult result) {
        model.addAttribute("breadcrumbs", GENRE_LIST_BREADCRUMBS);

        if (result.hasErrors()) {
            return "genre-save";
        }

        genreService.save(genre);
        return "redirect:/genre";
    }

    @GetMapping("/genre/delete")
    public String removeGenre(@RequestParam Optional<Long> id) {
        id.ifPresent(genreService::delete);
        return "redirect:/genre";
    }

}
