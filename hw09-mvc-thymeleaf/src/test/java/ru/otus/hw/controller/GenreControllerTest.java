package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.GenreService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@DisplayName("Тесты контроллера раздела 'Жанры'")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("должен возвращать статус 200 и список жанров для пути /genre")
    public void shouldReturnGenreList() throws Exception {
        given(genreService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/genre"))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-list"))
            .andExpect(model().attributeExists("genres"));
    }

    @Test
    @DisplayName("должен возвращать статус 200 и форму для создания нового жанра")
    public void shouldReturnSaveGenreFormForNewGenre() throws Exception {
        mockMvc.perform(get("/genre/save"))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-save"))
            .andExpect(model().attributeExists("genre"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен возвращать статус 200 и форму для редактирования существующего жанра")
    public void shouldReturnSaveGenreFormForExistingGenre() throws Exception {
        Genre genre = new Genre();
        genre.setId(1L);
        given(genreService.findById(1L)).willReturn(Optional.of(genre));

        mockMvc.perform(get("/genre/save").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-save"))
            .andExpect(model().attributeExists("genre"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен сохранять жанр и перенаправлять на список жанров при успешной валидации")
    public void shouldSaveGenreAndRedirectToListOnSuccess() throws Exception {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Genre_1");

        mockMvc.perform(post("/genre/save")
                .flashAttr("genre", genre))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/genre"));
    }

    @Test
    @DisplayName("должен возвращать форму с ошибками валидации при невалидных данных")
    public void shouldReturnSaveGenreFormWithValidationErrors() throws Exception {
        Genre invalidGenre = new Genre();
        invalidGenre.setName(""); // Пустое имя, что вызовет ошибку валидации

        mockMvc.perform(post("/genre/save")
                .flashAttr("genre", invalidGenre))
            .andExpect(status().isOk())
            .andExpect(view().name("genre-save"))
            .andExpect(model().attributeHasErrors("genre"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен удалять жанр и перенаправлять на список жанров")
    public void shouldDeleteGenreAndRedirectToList() throws Exception {
        mockMvc.perform(get("/genre/delete").param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/genre"));
    }
}