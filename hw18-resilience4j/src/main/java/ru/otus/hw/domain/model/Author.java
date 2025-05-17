package ru.otus.hw.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    private Long id;

    @Size(min = 1, max = 255, message = "{author.fullNameSizeValid}")
    @NotNull(message = "{author.fullNameNotEmpty}")
    private String fullName;
}
