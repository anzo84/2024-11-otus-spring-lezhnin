package ru.otus.hw.domain.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Genre {

    private Long id;

    @Size(min = 1, max = 255, message = "{genre.nameSizeValid}")
    private String name;
}
