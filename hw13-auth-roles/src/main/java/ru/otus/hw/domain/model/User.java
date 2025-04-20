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
public class User {

    private Long id;

    @Size(min = 1, max = 255, message = "{author.fullNameSizeValid}")
    @NotNull(message = "{author.fullNameNotEmpty}")
    private String username;

    @Size(min = 1, max = 255, message = "{author.fullNameSizeValid}")
    @NotNull(message = "{author.fullNameNotEmpty}")
    private String password;

    @NotEmpty(message = "{user.roleListNotEmpty}")
    private List<Role> roles;
}
