package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.domain.model.Role;
import ru.otus.hw.domain.model.User;
import ru.otus.hw.domain.service.UserService;
import ru.otus.hw.rest.model.RoleDto;
import ru.otus.hw.rest.model.UserDto;
import ru.otus.hw.security.SecurityHelper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@Import({SecurityConfiguration.class, SecurityHelper.class})
@ComponentScan(basePackages = "ru.otus.hw.rest.mapper")
@DisplayName("Тесты контроллера пользователей")
@WithMockUser(roles = "ADMINISTRATOR")
class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final User testUser = new User(1L, "testUser", "password", List.of(Role.ADMINISTRATOR));

    @Test
    @DisplayName("Получение пользователя по ID - успешно")
    void shouldReturnUserById() throws Exception {
        given(userService.findById(1L)).willReturn(Optional.of(testUser));

        mvc.perform(get("/api/users/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.roles[0]").value("ADMINISTRATOR"));

        verify(userService).findById(1L);
    }

    @Test
    @DisplayName("Получение пользователя по ID - не найдено")
    void shouldReturnNotFoundForUnknownUser() throws Exception {
        given(userService.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(get("/api/users/999")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(userService).findById(999L);
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void shouldReturnAllUsers() throws Exception {
        given(userService.findAll()).willReturn(List.of(testUser));

        mvc.perform(get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].username").value("testUser"));

        verify(userService).findAll();
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void shouldCreateUser() throws Exception {
        given(userService.save(any())).willReturn(testUser);

        UserDto requestDto = new UserDto();
        requestDto.setUsername("testUser");
        requestDto.setPassword("password");
        requestDto.setRoles(List.of(RoleDto.ADMINISTRATOR));

        mvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));

        verify(userService).save(any());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void shouldUpdateUser() throws Exception {
        given(userService.save(any())).willReturn(testUser);

        UserDto requestDto = new UserDto();
        requestDto.setUsername("updatedUser");
        requestDto.setPassword("newPassword");
        requestDto.setRoles(List.of(RoleDto.ADMINISTRATOR));

        mvc.perform(put("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));

        verify(userService).save(any());
    }

    @Test
    @DisplayName("Удаление пользователя")
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/api/users/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(userService).delete(1L);
    }

}