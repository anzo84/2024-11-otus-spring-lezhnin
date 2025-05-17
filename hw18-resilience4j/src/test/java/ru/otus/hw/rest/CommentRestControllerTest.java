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
import ru.otus.hw.domain.model.Comment;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.rest.model.CommentDto;
import ru.otus.hw.rest.model.ModifyCommentDto;
import ru.otus.hw.security.SecurityHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentRestController.class)
@ComponentScan(basePackages = "ru.otus.hw.rest.mapper")
@Import({SecurityConfiguration.class, SecurityHelper.class})
class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("POST /api/comments - создание комментария")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldCreateCommentAndReturnOk() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("Test Comment");
        commentDto.setBook(null);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        given(commentService.save(comment)).willReturn(comment);

        mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.content").value("Test Comment"));
    }

    @Test
    @DisplayName("DELETE /api/comments/{id} - удаление комментария")
    @WithMockUser(roles = "AUTHOR")
    void shouldDeleteCommentAndReturnOk() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/comments/{id}", id))
            .andExpect(status().isOk());

        verify(commentService, times(1)).deleteComment(id);
    }

    @Test
    @DisplayName("GET /api/comments - получение всех комментариев для книги")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldGetAllCommentsAndReturnOk() throws Exception {
        Long bookId = 1L;
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        List<Comment> comments = Collections.singletonList(comment);
        given(commentService.findByBookId(bookId)).willReturn(comments);

        mockMvc.perform(get("/api/comments").param("bookId", bookId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].content").value("Test Comment"));
    }

    @Test
    @DisplayName("GET /api/comments/{id} - получение комментария по ID (найден)")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldGetCommentByIdAndReturnOkWhenCommentExists() throws Exception {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("Test Comment");

        given(commentService.findById(id)).willReturn(Optional.of(comment));

        mockMvc.perform(get("/api/comments/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.content").value("Test Comment"));
    }

    @Test
    @DisplayName("GET /api/comments/{id} - получение комментария по ID (не найден)")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldReturnNotFoundWhenCommentDoesNotExist() throws Exception {
        Long id = 1L;
        given(commentService.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/comments/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/comments/{id} - обновление комментария")
    @WithMockUser(roles = "COMMENTATOR")
    void shouldUpdateCommentAndReturnOk() throws Exception {
        Long id = 1L;
        ModifyCommentDto updateCommentRequestDto = new ModifyCommentDto();
        updateCommentRequestDto.setContent("Updated Comment");
        updateCommentRequestDto.setBook(null);

        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent("Updated Comment");

        given(commentService.save(comment)).willReturn(comment);

        mockMvc.perform(put("/api/comments/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.content").value("Updated Comment"));
    }
}