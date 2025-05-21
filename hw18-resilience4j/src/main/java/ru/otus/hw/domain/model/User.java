package ru.otus.hw.domain.model;

import jakarta.validation.constraints.AssertTrue;
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

    @Size(min = 1, max = 255, message = "{user.fullNameSizeValid}")
    @NotNull(message = "{user.userNameNotEmpty}")
    private String username;

    private String password;

    @NotEmpty(message = "{user.roleListNotEmpty}")
    private List<Role> roles;

    @AssertTrue(message = "{user.passwordComplex}")
    private boolean checkPasswordComplexity() {
        if (password == null || password.isEmpty()) {
            return true;
        }
        return password.matches(".*[A-Z].*")
            && password.matches(".*[a-z].*")
            && password.matches(".*\\d.*")
            && password.matches(".*[!@#$%^&*()].*");
    }

    @AssertTrue(message = "{user.passwordSizeValid}")
    private boolean checkPasswordSize() {
        if (password == null || password.isEmpty()) {
            return true;
        }
        return password.length() >= 8 && password.length() <= 100;
    }
}
