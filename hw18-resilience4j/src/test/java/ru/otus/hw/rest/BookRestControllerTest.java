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
import ru.otus.hw.domain.model.Author;
import ru.otus.hw.domain.model.Book;
import ru.otus.hw.domain.model.Genre;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.rest.model.AuthorDto;
import ru.otus.hw.rest.model.BookDto;
import ru.otus.hw.rest.model.GenreDto;
import ru.otus.hw.rest.model.ModifyBookDto;
import ru.otus.hw.security.SecurityHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
@ComponentScan(basePackages = "ru.otus.hw.rest.mapper")
@Import({SecurityConfiguration.class, SecurityHelper.class})
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("POST /api/books - создание книги")
    @WithMockUser(roles = "AUTHOR")
    void shouldCreateBookAndReturnOk() throws Exception {

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setFullName("Test Author");

        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Test Genre");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor(authorDto);
        bookDto.setGenres(List.of(genreDto));

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        Author author = new Author();
        author.setId(1L);
        author.setFullName("Test Author");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        book.setAuthor(author);
        book.setGenres(List.of(genre));

        given(bookService.save(book)).willReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - удаление книги")
    @WithMockUser(roles = "AUTHOR")
    void shouldDeleteBookAndReturnOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/books/{id}", id))
            .andExpect(status().isOk());

        verify(bookService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("GET /api/books - получение всех книг")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldGetAllBooksAndReturnOk() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        List<Book> books = Collections.singletonList(book);
        given(bookService.findAll()).willReturn(books);

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    @DisplayName("GET /api/books/{id} - получение книги по ID (найден)")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldGetBookByIdAndReturnOkWhenBookExists() throws Exception {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Test Book");

        given(bookService.findById(id)).willReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @DisplayName("GET /api/books/{id} - получение книги по ID (не найден)")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        Long id = 1L;
        given(bookService.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/books/{id} - обновление книги")
    @WithMockUser(roles = "AUTHOR")
    void shouldUpdateBookAndReturnOk() throws Exception {

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setFullName("Test Author");

        GenreDto genreDto = new GenreDto();
        genreDto.setId(1L);
        genreDto.setName("Test Genre");

        Long id = 1L;
        ModifyBookDto updateBookRequestDto = new ModifyBookDto();
        updateBookRequestDto.setTitle("Updated Book");
        updateBookRequestDto.setAuthor(authorDto);
        updateBookRequestDto.setGenres(List.of(genreDto));

        Book book = new Book();
        book.setId(id);
        book.setTitle("Updated Book");

        Author author = new Author();
        author.setId(1L);
        author.setFullName("Test Author");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Test Genre");

        book.setAuthor(author);
        book.setGenres(List.of(genre));

        given(bookService.save(book)).willReturn(book);

        mockMvc.perform(put("/api/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Updated Book"));
    }
}