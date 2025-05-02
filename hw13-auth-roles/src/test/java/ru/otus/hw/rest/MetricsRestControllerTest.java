package ru.otus.hw.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.domain.service.AuthorService;
import ru.otus.hw.domain.service.BookService;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.domain.service.GenreService;
import ru.otus.hw.domain.service.UserService;
import ru.otus.hw.rest.model.MetricDto;
import ru.otus.hw.security.SecurityHelper;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetricsRestController.class)
@Import({SecurityConfiguration.class, SecurityHelper.class})
@WithMockUser(roles = "ADMINISTRATOR")
class MetricsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /api/metrics - получение метрик")
    void shouldGetMetricsAndReturnOk() throws Exception {
        given(authorService.count()).willReturn(10L);
        given(bookService.count()).willReturn(20L);
        given(genreService.count()).willReturn(30L);
        given(commentService.count()).willReturn(40L);
        given(userService.count()).willReturn(2L);

        mockMvc.perform(get("/api/metrics")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("userCount"))
            .andExpect(jsonPath("$[0].value").value(2L))
            .andExpect(jsonPath("$[1].name").value("authorCount"))
            .andExpect(jsonPath("$[1].value").value(10L))
            .andExpect(jsonPath("$[2].name").value("genreCount"))
            .andExpect(jsonPath("$[2].value").value(30L))
            .andExpect(jsonPath("$[3].name").value("bookCount"))
            .andExpect(jsonPath("$[3].value").value(20L))
            .andExpect(jsonPath("$[4].name").value("commentCount"))
            .andExpect(jsonPath("$[4].value").value(40L));
    }
}