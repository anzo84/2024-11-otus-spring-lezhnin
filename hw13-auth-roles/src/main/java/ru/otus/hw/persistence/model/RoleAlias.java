package ru.otus.hw.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleAlias {

    ADMINISTRATOR("ADMINISTRATOR"),
    AUTHOR("AUTHOR"),
    COMMENTATOR("COMMENTATOR");

    private final String roleName;
}
