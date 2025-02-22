package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.GenreService;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Controller
@RequiredArgsConstructor
public class BookController {

    public static final Map<String, String> BOOK_LIST_BREADCRUMBS = Map.of("book.list", "/book");

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("book")
    public String bookList(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "book-list";
    }

    @GetMapping("book/save")
    public String bookBook(@RequestParam Optional<Long> id, Model model) {
        Book book = id.map(bookService::findById).flatMap(Function.identity()).orElse(new Book());
        prepareSaveBookModel(model, book);
        return "book-save";
    }

    @PostMapping("book/save")
    public String saveBook(Model model, @Valid Book book, BindingResult result) {
        prepareSaveBookModel(model, book);
        if (result.hasErrors()) {
            return "book-save";
        }
        bookService.save(book);
        return "redirect:/book";
    }

    private void prepareSaveBookModel(Model model, Book book) {
        model.addAttribute("book", book);
        model.addAttribute("allgenres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("breadcrumbs", BOOK_LIST_BREADCRUMBS);
    }

    @GetMapping("/book/delete")
    public RedirectView remove(@RequestParam Optional<Long> id) {
        id.ifPresent(bookService::deleteById);
        return new RedirectView("book");
    }

}
