package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Репозиторий на основе MONGODB для работы с комментариями ")
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CommentRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен найти комментарий по id")
    @Test
    void shouldFindCommentById() {
        String id = "2";
        var expected = Optional.ofNullable(mongoTemplate.findById(id, Comment.class));
        var actual = repository.findById(id);
        assertThat(actual)
            .isNotEmpty()
            .map(Comment::getContent)
            .contains("Comment-2");
    }

    @DisplayName("должен загружать список комментариев для книги")
    @Test
    void shouldFindCommentListByBookId() {
        String bookId = "1";
        Book book = bookRepository.findById(bookId).orElseThrow(NoSuchElementException::new);
        var actualComments = repository.findByBook(book);
        assertThat(actualComments)
            .hasSize(3)
            .map(Comment::getContent)
            .contains("Updated comment content", "Comment-4", "New test comment");
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteCommentById() {
        String id = "1";
        repository.deleteById(id);
        var comment = mongoTemplate.findById(id, Comment.class);
        assertThat(comment).isNull();
    }

    @DisplayName("должен добавить комментарий")
    @Test
    void shouldInsertComment() {
        String bookId = "1";
        var book = bookRepository.findById(bookId).orElseThrow();
        var comment = Comment.builder()
            .book(book)
            .content("New test comment")
            .build();
        repository.save(comment);

        var actualComments = repository.findByBook(book);
        assertThat(actualComments)
            .hasSize(3)
            .map(Comment::getContent)
            .contains("Updated comment content", "Comment-4", "New test comment");
    }

    @DisplayName("должен обновить комментарий")
    @Test
    void shouldUpdateComment() {
        String id = "1";
        var comment = repository.findById(id).orElseThrow();
        comment.setContent("Updated comment content");
        repository.save(comment);

        var actualComment = mongoTemplate.findById(id, Comment.class);
        assertThat(actualComment)
            .isNotNull()
            .hasFieldOrPropertyWithValue("content", "Updated comment content");
    }

    @DisplayName("должен вернуть исключение при обновлении комментария с неизвестной книгой")
    @Test
    void shouldThrowThenUpdateCommentWithUnknownBook() {
        assertThrows(NoSuchElementException.class, () -> {
            var comment = new Comment("100", "New test comment", bookRepository.findById("100").get());
            comment.setContent("Updated comment content");
            repository.save(comment);
        });
    }

}