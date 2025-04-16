package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.persistence.model.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserEntity entity);

    List<User> map(List<UserEntity> entities);


}
