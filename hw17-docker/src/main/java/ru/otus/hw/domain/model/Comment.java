package ru.otus.hw.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;

    @Size(min = 1, max = 255, message = "{comment.contentSizeValid}")
    @NotNull(message = "{comment.contentNotEmpty}")
    private String content;

    @NotNull(message = "{comment.bookNotEmpty}")
    private Book book;
}
