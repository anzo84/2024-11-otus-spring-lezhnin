package ru.otus.hw.domain.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private Long id;

    @Size(min = 1, max = 255, message = "{book.titleSizeValid}")
    private String title;

    @NotNull(message = "{book.authorNotEmpty}")
    private Author author;

    @NotEmpty(message = "{book.genreListNotEmpty}")
    private List<Genre> genres;
}
