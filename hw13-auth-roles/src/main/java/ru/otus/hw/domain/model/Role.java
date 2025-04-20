package ru.otus.hw.domain.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMINISTRATOR("ADMINISTRATOR"),
    AUTHOR("AUTHOR"),
    COMMENTATOR("COMMENTATOR");

    private final String roleAlias;

    @Override
    public String getAuthority() {
        return this.roleAlias;
    }
}
