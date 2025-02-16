package ru.otus.hw.domain.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Author {

    private Long id;

    @Size(min = 1, max = 255, message = "{author.fullNameSizeValid}")
    private String fullName;
}
