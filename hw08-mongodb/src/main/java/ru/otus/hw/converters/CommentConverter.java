package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        return "Id: %s, Content: %s (Book Id: %s Title: %s)".formatted(comment.getId(), comment.getContent(),
            comment.getBook().getId(), comment.getBook().getTitle());
    }
}
