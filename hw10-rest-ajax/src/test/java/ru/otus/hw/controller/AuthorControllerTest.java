package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.service.AuthorService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@DisplayName("Тесты контроллера раздела 'Авторы'")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("должен возвращать статус 200 и список авторов для пути /author")
    public void shouldReturnAuthorList() throws Exception {
        given(authorService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/author"))
            .andExpect(status().isOk())
            .andExpect(view().name("author-list"))
            .andExpect(model().attributeExists("authors"));
    }

    @Test
    @DisplayName("должен возвращать статус 200 и форму для создания нового автора")
    public void shouldReturnSaveAuthorFormForNewAuthor() throws Exception {
        mockMvc.perform(get("/author/save"))
            .andExpect(status().isOk())
            .andExpect(view().name("author-save"))
            .andExpect(model().attributeExists("author"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен возвращать статус 200 и форму для редактирования существующего автора")
    public void shouldReturnSaveAuthorFormForExistingAuthor() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("Author_1");
        given(authorService.findById(1L)).willReturn(Optional.of(author));

        mockMvc.perform(get("/author/save").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("author-save"))
            .andExpect(model().attributeExists("author"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен сохранять автора и перенаправлять на список авторов при успешной валидации")
    public void shouldSaveAuthorAndRedirectToListOnSuccess() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("Author_1");

        mockMvc.perform(post("/author/save")
                .flashAttr("author", author))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/author"));
    }

    @Test
    @DisplayName("должен возвращать форму с ошибками валидации при невалидных данных")
    public void shouldReturnSaveAuthorFormWithValidationErrors() throws Exception {
        Author invalidAuthor = new Author();
        invalidAuthor.setFullName(""); // Пустое имя, что вызовет ошибку валидации

        mockMvc.perform(post("/author/save")
                .flashAttr("author", invalidAuthor))
            .andExpect(status().isOk())
            .andExpect(view().name("author-save"))
            .andExpect(model().attributeHasErrors("author"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен удалять автора и перенаправлять на список авторов")
    public void shouldDeleteAuthorAndRedirectToList() throws Exception {
        mockMvc.perform(get("/author/delete").param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/author"));
    }
}