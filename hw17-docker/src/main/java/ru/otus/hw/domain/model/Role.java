package ru.otus.hw.domain.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    AUTHOR("ROLE_AUTHOR"),
    COMMENTATOR("ROLE_COMMENTATOR");

    private final String roleAlias;

    @Override
    public String getAuthority() {
        return this.roleAlias;
    }
}
