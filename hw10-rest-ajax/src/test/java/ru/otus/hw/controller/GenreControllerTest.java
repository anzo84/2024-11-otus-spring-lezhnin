package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@DisplayName("Тесты контроллера раздела 'Жанры'")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("должен возвращать статус 200 и список жанров для пути /genre")
    public void shouldReturnGenreList() throws Exception {
        mockMvc.perform(get("/genre"))
            .andExpect(status().isOk());
    }
}