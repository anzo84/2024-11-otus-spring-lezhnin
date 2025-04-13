package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoutingController.class)
@DisplayName("Тесты контроллера раздела 'Жанры'")
@WithMockUser
@Import(SecurityConfiguration.class)
public class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("должен возвращать статус 200 и список жанров для пути /genre")
    public void shouldReturnGenrePage() throws Exception {
        mockMvc.perform(get("/genre"))
            .andExpect(status().isOk());
    }
}