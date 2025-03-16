package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.rest.model.GenreDto;
import ru.otus.hw.rest.model.ModifyGenreDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreRestController.class)
@ComponentScan(basePackages = "ru.otus.hw.rest.mapper")
class GenreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("POST /api/genres - создание жанра")
    void shouldCreateGenreAndReturnOk() throws Exception {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Test Genre");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        given(genreService.save(genre)).willReturn(genre);

        mockMvc.perform(post("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Test Genre"));
    }

    @Test
    @DisplayName("DELETE /api/genres/{id} - удаление жанра")
    void shouldDeleteGenreAndReturnOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/genres/{id}", id))
            .andExpect(status().isOk());

        verify(genreService, times(1)).delete(id);
    }

    @Test
    @DisplayName("GET /api/genres - получение всех жанров")
    void shouldGetAllGenresAndReturnOk() throws Exception {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        List<Genre> genres = Collections.singletonList(genre);
        given(genreService.findAll()).willReturn(genres);

        mockMvc.perform(get("/api/genres"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("Test Genre"));
    }

    @Test
    @DisplayName("GET /api/genres/{id} - получение жанра по ID (найден)")
    void shouldGetGenreByIdAndReturnOkWhenGenreExists() throws Exception {
        Long id = 1L;
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName("Test Genre");

        given(genreService.findById(id)).willReturn(Optional.of(genre));

        mockMvc.perform(get("/api/genres/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Test Genre"));
    }

    @Test
    @DisplayName("GET /api/genres/{id} - получение жанра по ID (не найден)")
    void shouldReturnNotFoundWhenGenreDoesNotExist() throws Exception {
        Long id = 1L;
        given(genreService.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/genres/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/genres/{id} - обновление жанра")
    void shouldUpdateGenreAndReturnOk() throws Exception {
        Long id = 1L;
        ModifyGenreDto updateGenreRequestDto = new ModifyGenreDto();
        updateGenreRequestDto.setName("Updated Genre");

        Genre genre = new Genre();
        genre.setId(id);
        genre.setName("Updated Genre");

        given(genreService.save(genre)).willReturn(genre);

        mockMvc.perform(put("/api/genres/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGenreRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Updated Genre"));
    }
}