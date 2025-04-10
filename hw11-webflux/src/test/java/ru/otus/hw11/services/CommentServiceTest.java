package ru.otus.hw11.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw11.domain.exception.EntityNotFoundException;
import ru.otus.hw11.domain.model.Book;
import ru.otus.hw11.domain.model.Comment;
import ru.otus.hw11.domain.service.BookService;
import ru.otus.hw11.domain.service.BookServiceImpl;
import ru.otus.hw11.domain.service.CommentService;
import ru.otus.hw11.domain.service.CommentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataR2dbcTest
@DisplayName("Реактивный тест сервиса комментариев")
@ComponentScan(basePackages = "ru.otus.hw11.mapper")
@Import({BookServiceImpl.class, CommentServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @Test
    @Order(1)
    @DisplayName("должен найти комментарии по ID книги")
    void shouldFindCommentsByBookId() {
        StepVerifier.create(commentService.findByBookId(1L).collectList())
            .assertNext(comments -> {
                assertThat(comments)
                    .extracting(Comment::getId, Comment::getContent)
                    .containsExactlyInAnyOrder(
                        tuple(1L, "Comment 1_1"),
                        tuple(2L, "Comment 1_2")
                    );
            })
            .verifyComplete();
    }

    @Test
    @Order(2)
    @DisplayName("должен вернуть пустой Flux при поиске комментариев для несуществующей книги")
    void shouldReturnEmptyFluxForUnknownBook() {
        StepVerifier.create(commentService.findByBookId(100L))
            .verifyComplete();
    }

    @Test
    @Order(3)
    @DisplayName("должен добавить новый комментарий для книги")
    void shouldInsertCommentForBook() {
        // 1. Получаем книгу и создаем новый комментарий
        Mono<Comment> saveOperation = bookService.findById(1L)
            .map(book -> new Comment(null, "Comment 1_3", book))
            .flatMap(comment -> commentService.save(Mono.just(comment)));

        // 2. Проверяем результат сохранения
        StepVerifier.create(saveOperation)
            .assertNext(savedComment -> {
                assertThat(savedComment.getId()).isNotNull();
                assertThat(savedComment.getContent()).isEqualTo("Comment 1_3");
                assertThat(savedComment.getBook()).isNotNull();
                assertThat(savedComment.getBook().getId()).isEqualTo(1L);
            })
            .verifyComplete();

        // 3. Проверяем, что комментарий добавился в список для книги
        StepVerifier.create(commentService.findByBookId(1L).collectList())
            .assertNext(comments -> {
                assertThat(comments)
                    .hasSize(3)
                    .extracting(Comment::getContent)
                    .containsExactlyInAnyOrder(
                        "Comment 1_1",
                        "Comment 1_2",
                        "Comment 1_3"
                    );
            })
            .verifyComplete();
    }

    @Test
    @Order(4)
    @DisplayName("должен бросить исключение при попытке добавить комментарий для несуществующей книги")
    void shouldThrowExceptionForInsertThenUnknownBook() {
        Mono<Comment> comment = Mono.just(new Comment(null, "Comment 1_3", new Book(100L, "Book", null, null)));

        StepVerifier.create(commentService.save(comment))
            .expectErrorMatches(throwable ->
                throwable instanceof EntityNotFoundException &&
                    throwable.getMessage().equals("Book with id 100 not found"))
            .verify();
    }

    @Test
    @Order(5)
    @DisplayName("должен изменить существующий комментарий")
    void shouldUpdateComment() {
        // 1. Создаем Mono с обновленным комментарием
        Mono<Comment> updateOperation = commentService.findById(1L)
            .map(comment -> {
                comment.setContent("Updated comment");
                return comment;
            })
            .flatMap(comment -> commentService.save(Mono.just(comment)));

        // 2. Проверяем операцию обновления и результат
        StepVerifier.create(updateOperation)
            .assertNext(updatedComment -> {
                assertThat(updatedComment.getId()).isEqualTo(1L);
                assertThat(updatedComment.getContent()).isEqualTo("Updated comment");
            })
            .verifyComplete();

        // 3. Проверяем, что комментарии для книги содержат обновленный
        StepVerifier.create(commentService.findByBookId(1L).collectList())
            .assertNext(comments -> {
                assertThat(comments)
                    .extracting(Comment::getId, Comment::getContent)
                    .containsExactlyInAnyOrder(
                        tuple(1L, "Updated comment"),
                        tuple(2L, "Comment 1_2"),
                        tuple(4L, "Comment 1_3")
                    );
            })
            .verifyComplete();
    }

    @Test
    @Order(6)
    @DisplayName("должен удалить комментарий")
    void shouldDeleteComment() {
        StepVerifier.create(
                commentService.delete(1L)
                    .then(commentService.findByBookId(1L).collectList())
            )
            .assertNext(comments -> {
                assertThat(comments)
                    .extracting(Comment::getId, Comment::getContent)
                    .containsExactly(
                        tuple(2L, "Comment 1_2"),
                        tuple(4L, "Comment 1_3")
                    );
            })
            .verifyComplete();
    }
}