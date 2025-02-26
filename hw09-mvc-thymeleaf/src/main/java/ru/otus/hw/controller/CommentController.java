package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {

    public static final Map<String, String> COMMENT_LIST_BREADCRUMBS = Map.of("comment.list", "/comment");

    private final CommentService commentService;

    private final BookService bookService;

    @RequestMapping(value = "comment", method = {RequestMethod.GET, RequestMethod.POST})
    public String commentList(@RequestParam Map<String, String> params, Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        if (!books.isEmpty()) {
            long id = Long.parseLong(params.getOrDefault("bookId", books.get(0).getId().toString()));
            model.addAttribute("bookId", id);
            model.addAttribute("comments", commentService.findByBookId(id));
        }
        return "comment-list";
    }

    @GetMapping("comment/save")
    public String saveComment(@RequestParam Map<String, String> params, Model model) {
        try {
            Comment comment = getOrCreateComment(params);

            if (comment.getBook() == null) {
                comment.setBook(getBookFromParams(params)
                    .orElseThrow(() -> new InvalidParameterException("Book not found")));
            }

            model.addAttribute("breadcrumbs", COMMENT_LIST_BREADCRUMBS);
            model.addAttribute("comment", comment);
            return "comment-save";
        } catch (NumberFormatException | InvalidParameterException e) {
            return "redirect:/comment";
        }
    }

    private Comment getOrCreateComment(Map<String, String> params) {
        return Optional.ofNullable(params.get("id"))
            .map(id -> commentService.findById(Long.parseLong(id)))
            .flatMap(comment -> comment)
            .orElse(new Comment());
    }

    private Optional<Book> getBookFromParams(Map<String, String> params) {
        return Optional.ofNullable(params.get("bookid"))
            .map(bookId -> bookService.findById(Long.parseLong(bookId)))
            .flatMap(book -> book);
    }

    @PostMapping("comment/save")
    public String saveComment(Model model, @Valid Comment comment, BindingResult result) {
        model.addAttribute("breadcrumbs", COMMENT_LIST_BREADCRUMBS);
        if (result.hasErrors()) {
            commentService.save(comment);
            return "comment-save";
        }
        model.addAttribute("comment", commentService.save(comment));
        return "redirect:/comment";
    }

    @GetMapping("/comment/delete")
    public RedirectView removeGenre(@RequestParam Optional<Long> id, RedirectAttributes attributes) {
        id.ifPresent(commentId -> {
            commentService.findById(commentId).ifPresent(comment -> {
                attributes.addAttribute("bookId", comment.getBook().getId());
                commentService.deleteComment(commentId);
            });
        });
        return new RedirectView("/comment");
    }
}
