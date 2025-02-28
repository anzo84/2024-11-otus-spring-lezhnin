package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.domain.service.GenreService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@DisplayName("Тесты контроллера главной страницы")
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("должен возвращать статус 200 и корректные атрибуты модели для корневого пути")
    public void shouldPrepareModelForHome() throws Exception {
        given(genreService.count()).willReturn(5L);
        given(authorService.count()).willReturn(10L);
        given(bookService.count()).willReturn(15L);
        given(commentService.count()).willReturn(20L);

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(model().attribute("genreCount", 5L))
            .andExpect(model().attribute("authorCount", 10L))
            .andExpect(model().attribute("bookCount", 15L))
            .andExpect(model().attribute("commentCount", 20L));

        mockMvc.perform(get("/home"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(model().attribute("genreCount", 5L))
            .andExpect(model().attribute("authorCount", 10L))
            .andExpect(model().attribute("bookCount", 15L))
            .andExpect(model().attribute("commentCount", 20L));
    }
}