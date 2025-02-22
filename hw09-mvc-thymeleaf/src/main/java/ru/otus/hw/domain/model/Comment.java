package ru.otus.hw.domain.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.otus.hw.persistence.model.BookEntity;

@Data
public class Comment {

    private Long id;

    @Size(min = 1, max = 255, message = "{comment.contentSizeValid}")
    private String content;

    @NotEmpty(message = "{comment.bookNotEmpty}")
    private BookEntity book;
}
