package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(key = "ca", value = "Show all comments for book")
    public String showComments(String bookId) {
        var comments = commentService.findByBookId(bookId)
            .stream()
            .map(commentConverter::commentToString)
            .collect(Collectors.toList());
        if (comments.isEmpty()) {
            return "Comment not found";
        }
        return String.join(System.lineSeparator(), comments);
    }

    @ShellMethod(key = "bcn", value = "Add new comment for book")
    public String addCommentForBook(String bookId, String content) {
        var comment = commentService.addComment(content, bookId);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(key = "cu", value = "Edit comment")
    public String updateComment(String commentId, String changedComment) {
        var comment = commentService.updateComment(commentId, changedComment);
        return commentConverter.commentToString(comment);
    }

    @ShellMethod(key = "cd", value = "Delete comment")
    public void deleteComment(String id) {
        commentService.deleteComment(id);
    }
}
