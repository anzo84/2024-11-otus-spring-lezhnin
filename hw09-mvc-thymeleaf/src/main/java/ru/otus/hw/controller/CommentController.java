package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {

    public static final Map<String, String> COMMENT_LIST_BREADCRUMBS = Map.of("comment.list", "comment");

    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping("comment")
    public String commentList(@RequestParam Optional<Long> id, Model model) {
        model.addAttribute("books", bookService.findAll());
        id.ifPresent(bookId -> {
            model.addAttribute("bookId", bookId);
            model.addAttribute("comments", commentService.findByBookId(bookId));
        });
        return "comment-list";
    }

    @PostMapping("comment/save")
    public String saveComment(Model model, @Valid Comment comment, BindingResult result) {
        prepareSaveCommentModel(model, comment);
        if (result.hasErrors()) {
            return "comment-save";
        }
        commentService.save(comment);
        return "redirect:/comment";
    }

    private void prepareSaveCommentModel(Model model, Comment comment) {
        model.addAttribute("comment", comment);
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("breadcrumbs", COMMENT_LIST_BREADCRUMBS);
    }

    @GetMapping("/comment/delete")
    public RedirectView removeGenre(@RequestParam Optional<Long> id, RedirectAttributes attributes) {
        id.ifPresent(commentId -> {
            Comment comment = commentService.findById(commentId);
            attributes.addAttribute("bookId", comment.getBook().getId());
            commentService.deleteComment(commentId);
        });
        return new RedirectView("comment");
    }
}
