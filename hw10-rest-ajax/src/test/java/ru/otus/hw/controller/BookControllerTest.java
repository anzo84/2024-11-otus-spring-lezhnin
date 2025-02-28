package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.GenreService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("Тесты контроллера раздела 'Книги'")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("должен вернут список книг")
    public void shouldReturnBookList() throws Exception {
        Book book = new Book();
        book.setId(1L);
        given(bookService.findById(1L)).willReturn(Optional.of(book));

        given(bookService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/book"))
            .andExpect(status().isOk())
            .andExpect(view().name("book-list"))
            .andExpect(model().attributeExists("books"));
    }

    @Test
    @DisplayName("должен заполнить модель для формы создания книги")
    public void shouldPrepareModelForBookSave() throws Exception {
        Book book = new Book();
        book.setId(1L);
        given(bookService.findById(1L)).willReturn(Optional.of(book));
        given(genreService.findAll()).willReturn(List.of(new Genre(1L, "Genre")));
        given(authorService.findAll()).willReturn(List.of(new Author(1L, "Author")));

        mockMvc.perform(get("/book/save").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("book-save"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("allgenres"))
            .andExpect(model().attributeExists("authors"));
    }

    @Test
    @DisplayName("должен сохранить книгу и перейти на страницу со списком книг")
    public void shouldSaveTheBook() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor(new Author(1L, "Author"));
        book.setGenres(List.of(new Genre(1L, "Genre")));

        mockMvc.perform(post("/book/save")
                .flashAttr("book", book))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/book"));
    }

    @Test
    @DisplayName("должен вернуть в модели ошибку и вернуться на страницу создания книги с кодом 200")
    public void shouldValidateErrorsThenSaveBook() throws Exception {
        Book invalidBook = new Book();
        invalidBook.setTitle(""); // Пустое название, что вызовет ошибку валидации

        given(genreService.findAll()).willReturn(Collections.emptyList());
        given(authorService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(post("/book/save")
                .flashAttr("book", invalidBook))
            .andExpect(status().isOk())
            .andExpect(view().name("book-save"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("allgenres"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeHasErrors("book")); // Проверяем, что есть ошибки валидации для объекта Book
    }

    @Test
    @DisplayName("должен удалить книгу")
    public void shouldDeleteTheBook() throws Exception {
        mockMvc.perform(get("/book/delete").param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/book"));
    }
}