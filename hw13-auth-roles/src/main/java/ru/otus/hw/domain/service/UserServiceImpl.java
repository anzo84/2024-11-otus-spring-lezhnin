package ru.otus.hw.domain.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.persistence.model.RoleEntity;
import ru.otus.hw.persistence.model.UserEntity;
import ru.otus.hw.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id).map(userMapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userMapper.map(userRepository.findAll());
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public User save(@Valid User user) {
        UserEntity entity = user.getId() == null ? new UserEntity() :
            userRepository.findById(user.getId()).orElse(new UserEntity());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRoles(user.getRoles().stream().map(role -> {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setUser(entity);
            roleEntity.setAlias(userMapper.map(role));
            return roleEntity;
        }).collect(Collectors.toList()));
        return userMapper.map(userRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
