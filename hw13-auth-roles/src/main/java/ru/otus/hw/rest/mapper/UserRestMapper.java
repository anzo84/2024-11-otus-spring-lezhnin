package ru.otus.hw.rest.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.model.RoleDescription;
import ru.otus.hw.rest.model.RoleDescriptionDto;
import ru.otus.hw.rest.model.RoleDto;
import ru.otus.hw.rest.model.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRestMapper {
    UserDto toDto(ru.otus.hw.domain.model.User user);

    List<UserDto> toDto(List<ru.otus.hw.domain.model.User> users);

    ru.otus.hw.domain.model.User map(UserDto user);

    List<ru.otus.hw.domain.model.User> map(List<UserDto> users);

    Role map(RoleDto roleDto);

    RoleDescriptionDto map(RoleDescription role);

    List<RoleDescriptionDto> mapList(List<RoleDescription> roles);

}
