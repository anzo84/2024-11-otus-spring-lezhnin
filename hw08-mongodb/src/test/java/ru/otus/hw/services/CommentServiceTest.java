package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@DisplayName("Интегро тест сервиса комментариев")
@Import({BookServiceImpl.class, CommentServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    @Order(1)
    @DisplayName("должен найти комментарии по ID книги")
    public void shouldFindBookById() {
        assertThat(commentService.findByBookId("1"))
            .extracting(Comment::getId, Comment::getContent)
            .containsExactlyInAnyOrder(
                tuple("1", "Comment 1_1"),
                tuple("2", "Comment 1_2")
            );
    }

    @Test
    @Order(2)
    @DisplayName("должен вернуть пустой список в случае поиска комментариев для не существующей книги")
    public void shouldReturnEmptyListThenUnknownBook() {
        assertThat(commentService.findByBookId("100"))
            .isNotNull()
            .isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("должен добавить новый комментарий для книги")
    public void shouldInsertCommentForBook() {
        commentService.addComment("Comment 1_3", "1");
        assertThat(commentService.findByBookId("1"))
            .map(Comment::getContent)
            .containsExactly("Comment-1", "Comment 1_2", "Comment 1_3");
    }

    @Test
    @Order(4)
    @DisplayName("должен бросить исключение при попытке добавить комментарий для несуществующей книги")
    public void shouldThrowExceptionForInsertThenUnknownBook() {
        Exception e = assertThrows(EntityNotFoundException.class,
            () -> commentService.addComment("new comment", "100"));
        assertThat(e.getMessage()).isEqualTo("Book with id 100 not found");
    }

    @Test
    @Order(5)
    @DisplayName("должен изменить существующий комментарий")
    public void shouldUpdateComment() {
        commentService.updateComment("1", "Updated comment");
        assertThat(commentService.findByBookId("1"))
            .map(Comment::getContent)
            .containsExactly("Updated comment", "Comment 1_2", "Comment 1_3");
    }

    @Test
    @Order(6)
    @DisplayName("должен бросить исключение при попытке обновить несуществующий комментарий")
    public void shouldThrowExceptionForUpdateThenUnknownComment() {
        Exception e = assertThrows(EntityNotFoundException.class,
            () -> commentService.updateComment("100", "Updated comment"));
        assertThat(e.getMessage()).isEqualTo("Comment with id 100 not found");
    }

    @Test
    @Order(7)
    @DisplayName("должен удалить комментарий")
    public void shouldDeleteComment() {
        commentService.deleteComment("1");
        assertThat(commentService.findByBookId("1"))
            .map(Comment::getContent)
            .containsExactly("Comment 1_2", "Comment 1_3");
    }

}