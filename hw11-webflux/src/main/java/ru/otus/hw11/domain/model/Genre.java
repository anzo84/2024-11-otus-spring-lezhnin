package ru.otus.hw11.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    private Long id;

    @Size(min = 1, max = 255, message = "{genre.nameSizeValid}")
    @NotNull(message = "{genre.nameNotEmpty}")
    private String name;
}
