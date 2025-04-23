package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.persistence.model.RoleAlias;
import ru.otus.hw.persistence.model.RoleEntity;
import ru.otus.hw.persistence.model.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User map(UserEntity entity);

    List<User> map(List<UserEntity> entities);

    Role map(RoleAlias roleAlias);

    RoleAlias map(Role role);

    default Role map(RoleEntity entity) {
        return map(entity.getAlias());
    }
}
