package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.model.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final RoleHierarchy roleHierarchy;

    public boolean hasRole(Role role) {
        return hasAnyRole(role);
    }

    public String hasRole(Role role, String allowValue) {
        return hasRole(role) ? allowValue : "";
    }

    public boolean hasAnyRole(Role... role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        List<String> paramAuthorities = Arrays.stream(role).map(Role::getAuthority).toList();
        return roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities())
            .stream()
            .anyMatch(item -> paramAuthorities.contains(item.getAuthority()));
    }

    public boolean hasRole(String alias) {
        return hasAnyRole(alias);
    }

    public boolean hasAnyRole(String... aliases) {
        Role[] roles = Arrays.stream(aliases)
            .map(alias -> findByAlias(alias).orElse(null))
            .filter(Objects::nonNull)
            .toArray(Role[]::new);
        return hasAnyRole(roles);
    }

    private Optional<Role> findByAlias(String alias) {
        return Arrays.stream(Role.values())
            .filter(item -> item.getAuthority().equalsIgnoreCase(alias) || item.name().equalsIgnoreCase(alias))
            .findFirst();
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
