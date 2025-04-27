package ru.otus.hw.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum RoleAlias {

    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    AUTHOR("ROLE_AUTHOR"),
    COMMENTATOR("ROLE_COMMENTATOR");

    private final String roleName;

    public static RoleAlias fromDbValue(String dbValue) {
        return Arrays.stream(values())
            .filter(role -> role.roleName.equals(dbValue))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown DB value: " + dbValue));
    }
}
