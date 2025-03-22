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
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.rest.model.AuthorDto;
import ru.otus.hw.rest.model.ModifyAuthorDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorRestController.class)
@ComponentScan(basePackages = "ru.otus.hw.rest.mapper")
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("POST /api/authors - создание автора")
    void shouldCreateAuthorAndReturnOk() throws Exception {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setFullName("Test Author");

        Author author = new Author();
        author.setId(1L);
        author.setFullName("Test Author");

        given(authorService.save(author)).willReturn(author);

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.fullName").value("Test Author"));
    }

    @Test
    @DisplayName("DELETE /api/authors/{id} - удаление автора")
    void shouldDeleteAuthorAndReturnOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/authors/{id}", id))
            .andExpect(status().isOk());

        verify(authorService, times(1)).delete(id);
    }

    @Test
    @DisplayName("GET /api/authors - получение всех авторов")
    void shouldGetAllAuthorsAndReturnOk() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("Test Author");

        List<Author> authors = Collections.singletonList(author);
        given(authorService.findAll()).willReturn(authors);

        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].fullName").value("Test Author"));
    }

    @Test
    @DisplayName("GET /api/authors/{id} - получение автора по ID (найден)")
    void shouldGetAuthorByIdAndReturnOkWhenAuthorExists() throws Exception {
        Long id = 1L;
        Author author = new Author();
        author.setId(id);
        author.setFullName("Test Author");

        given(authorService.findById(id)).willReturn(Optional.of(author));

        mockMvc.perform(get("/api/authors/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.fullName").value("Test Author"));
    }

    @Test
    @DisplayName("GET /api/authors/{id} - получение автора по ID (не найден)")
    void shouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        Long id = 1L;
        given(authorService.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/authors/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/authors/{id} - обновление автора")
    void shouldUpdateAuthorAndReturnOk() throws Exception {
        Long id = 1L;
        ModifyAuthorDto updateAuthorRequestDto = new ModifyAuthorDto();
        updateAuthorRequestDto.setFullName("Updated Author");

        Author author = new Author();
        author.setId(id);
        author.setFullName("Updated Author");

        given(authorService.save(author)).willReturn(author);

        mockMvc.perform(put("/api/authors/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAuthorRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.fullName").value("Updated Author"));
    }
}