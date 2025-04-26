package ru.otus.hw.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleAlias {

    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    AUTHOR("ROLE_AUTHOR"),
    COMMENTATOR("ROLE_COMMENTATOR");

    private final String roleName;
}
