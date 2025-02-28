package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.service.AuthorService;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    public static final Map<String, String> AUTHOR_LIST_BREADCRUMBS = Map.of("author.list", "/author");

    private final AuthorService authorService;

    @GetMapping("author")
    public String authorList(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "author-list";
    }

    @GetMapping("author/save")
    public String saveAuthor(@RequestParam Optional<Long> id, Model model) {
        Author author = id.map(authorService::findById).flatMap(Function.identity()).orElse(new Author());
        model.addAttribute("author", author);
        model.addAttribute("breadcrumbs", AUTHOR_LIST_BREADCRUMBS);
        return "author-save";
    }

    @PostMapping("author/save")
    public String saveAuthor(Model model, @Valid Author author, BindingResult result) {
        model.addAttribute("breadcrumbs", AUTHOR_LIST_BREADCRUMBS);

        if (result.hasErrors()) {
            return "author-save";
        }

        authorService.save(author);
        return "redirect:/author";
    }

    @GetMapping("/author/delete")
    public String removeAuthor(@RequestParam Optional<Long> id) {
        id.ifPresent(authorService::delete);
        return "redirect:/author";
    }

}
