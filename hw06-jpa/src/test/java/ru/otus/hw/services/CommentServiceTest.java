package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.CommentRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интегро тест сервиса комментариев")
@Import({BookServiceImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class,
    BookRepositoryImpl.class, CommentRepositoryImpl.class, CommentServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    @Order(1)
    @DisplayName("должен найти комментарии по ID книги")
    public void shouldFindBookById() {
        assertThat(commentService.findByBookId(1L))
            .extracting(Comment::getId, Comment::getContent)
            .containsExactlyInAnyOrder(
                tuple(1L, "Comment 1_1"),
                tuple(2L, "Comment 1_2")
            );
    }

    @Test
    @Order(2)
    @DisplayName("должен вернуть пустой список в случае поиска комментариев для не существующей книги")
    public void shouldReturnEmptyListThenUnknownBook() {
        assertThat(commentService.findByBookId(100L))
            .isNotNull()
            .isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("должен добавить новый комментарий для книги")
    public void shouldInsertCommentForBook() {
        commentService.addComment("Comment 1_3", 1L);
        assertThat(commentService.findByBookId(1L))
            .map(Comment::getContent)
            .containsExactly("Comment 1_1", "Comment 1_2", "Comment 1_3");
    }

    @Test
    @Order(4)
    @DisplayName("должен бросить исключение при попытке добавить комментарий для несуществующей книги")
    public void shouldThrowExceptionForInsertThenUnknownBook() {
        Exception e = assertThrows(EntityNotFoundException.class,
            () -> commentService.addComment("new comment", 100L));
        assertThat(e.getMessage()).isEqualTo("Book with id 100 not found");
    }

    @Test
    @Order(5)
    @DisplayName("должен изменить существующий комментарий")
    public void shouldUpdateComment() {
        commentService.updateComment(1L, "Updated comment");
        assertThat(commentService.findByBookId(1L))
            .map(Comment::getContent)
            .containsExactly("Updated comment", "Comment 1_2", "Comment 1_3");
    }

    @Test
    @Order(6)
    @DisplayName("должен бросить исключение при попытке обновить несуществующий комментарий")
    public void shouldThrowExceptionForUpdateThenUnknownComment() {
        Exception e = assertThrows(EntityNotFoundException.class,
            () -> commentService.updateComment(100L, "Updated comment"));
        assertThat(e.getMessage()).isEqualTo("Comment with id 100 not found");
    }

    @Test
    @Order(7)
    @DisplayName("должен удалить комментарий")
    public void shouldDeleteComment() {
        commentService.deleteComment(1L);
        assertThat(commentService.findByBookId(1L))
            .map(Comment::getContent)
            .containsExactly("Comment 1_2", "Comment 1_3");
    }

}