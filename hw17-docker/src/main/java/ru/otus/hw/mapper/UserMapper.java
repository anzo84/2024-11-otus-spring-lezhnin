package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.persistence.model.RoleAlias;
import ru.otus.hw.persistence.model.RoleEntity;
import ru.otus.hw.persistence.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User map(UserEntity entity);

    User mapWithPassword(UserEntity entity);

    Role map(RoleAlias roleAlias);

    RoleAlias map(Role role);

    default Role map(RoleEntity entity) {
        return map(entity.getAlias());
    }
}
