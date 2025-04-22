package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.service.UserService;
import ru.otus.hw.rest.api.UsersApi;
import ru.otus.hw.rest.mapper.UserRestMapper;
import ru.otus.hw.rest.model.ModifyUserDto;
import ru.otus.hw.rest.model.RoleDescriptionDto;
import ru.otus.hw.rest.model.UserDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController implements UsersApi {

    private final UserService service;

    private final UserRestMapper mapper;

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(userDto))));
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RoleDescriptionDto>> getAllRoles() {
        return ResponseEntity.ok(mapper.mapList(service.getRoleDescriptions(LocaleContextHolder.getLocale())));
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(mapper.toDto(service.findAll()));
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        return service.findById(Optional.ofNullable(id).orElse(0L))
            .map(value -> ResponseEntity.ok(mapper.toDto(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long id, ModifyUserDto modifyUserDto) {
        UserDto user = new UserDto();
        user.setId(id);
        user.setUsername(modifyUserDto.getUsername());
        user.setPassword(modifyUserDto.getPassword());
        user.setRoles(modifyUserDto.getRoles());
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(user))));
    }
}
