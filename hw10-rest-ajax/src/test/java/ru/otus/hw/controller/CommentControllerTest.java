package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@DisplayName("Тесты контроллера раздела 'Комментарии'")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("должен возвращать статус 200 и список комментариев для пути /comment")
    public void shouldReturnCommentList() throws Exception {
        Author author = new Author(1L, "Author_1");
        Genre genre = new Genre(1L, "Genre_1");
        Book book = new Book(1L, "Book_1", author, List.of(genre));

        given(bookService.findAll()).willReturn(List.of(book));
        given(commentService.findByBookId(1L)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/comment").param("bookId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("comment-list"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attributeExists("comments"))
            .andExpect(model().attributeExists("bookId"));
    }

    @Test
    @DisplayName("должен возвращать статус 200 и форму для создания нового комментария")
    public void shouldReturnSaveCommentFormForNewComment() throws Exception {
        Author author = new Author(1L, "Author_1");
        Genre genre = new Genre(1L, "Genre_1");
        Book book = new Book(1L, "Book_1", author, List.of(genre));

        given(bookService.findById(1L)).willReturn(Optional.of(book));

        mockMvc.perform(get("/comment/save").param("bookid", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("comment-save"))
            .andExpect(model().attributeExists("comment"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен возвращать статус 200 и форму для редактирования существующего комментария")
    public void shouldReturnSaveCommentFormForExistingComment() throws Exception {
        Author author = new Author(1L, "Author_1");
        Genre genre = new Genre(1L, "Genre_1");
        Book book = new Book(1L, "Book_1", author, List.of(genre));

        Comment comment = new Comment(1L, "Comment_1", book);

        given(commentService.findById(1L)).willReturn(Optional.of(comment));

        mockMvc.perform(get("/comment/save").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("comment-save"))
            .andExpect(model().attributeExists("comment"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен сохранять комментарий и перенаправлять на список комментариев при успешной валидации")
    public void shouldSaveCommentAndRedirectToListOnSuccess() throws Exception {
        Author author = new Author(1L, "Author_1");
        Genre genre = new Genre(1L, "Genre_1");
        Book book = new Book(1L, "Book_1", author, List.of(genre));

        Comment comment = new Comment(1L, "Comment_1", book);

        mockMvc.perform(post("/comment/save")
                .flashAttr("comment", comment))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/comment"));
    }

    @Test
    @DisplayName("должен возвращать форму с ошибками валидации при невалидных данных")
    public void shouldReturnSaveCommentFormWithValidationErrors() throws Exception {
        Author author = new Author(1L, "Author_1");
        Genre genre = new Genre(1L, "Genre_1");
        Book book = new Book(1L, "Book_1", author, List.of(genre));

        Comment invalidComment = new Comment(1L, "", book); // Пустой текст, что вызовет ошибку валидации

        mockMvc.perform(post("/comment/save")
                .flashAttr("comment", invalidComment))
            .andExpect(status().isOk())
            .andExpect(view().name("comment-save"))
            .andExpect(model().attributeHasErrors("comment"))
            .andExpect(model().attributeExists("breadcrumbs"));
    }

    @Test
    @DisplayName("должен удалять комментарий и перенаправлять на список комментариев")
    public void shouldDeleteCommentAndRedirectToList() throws Exception {
        Author author = new Author(1L, "Author_1");
        Genre genre = new Genre(1L, "Genre_1");
        Book book = new Book(1L, "Book_1", author, List.of(genre));

        Comment comment = new Comment(1L, "Comment_1", book);

        given(commentService.findById(1L)).willReturn(Optional.of(comment));

        mockMvc.perform(get("/comment/delete").param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/comment?bookId=1"));
    }
}