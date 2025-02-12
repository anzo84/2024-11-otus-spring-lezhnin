package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({CommentRepositoryImpl.class, BookRepositoryImpl.class})
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен найти комментарий по id")
    @Test
    void shouldFindCommentById() {
        long id = 1L;
        var expected = em.find(Comment.class, id);
        var actual = repository.findById(id);
        assertThat(actual)
            .isNotEmpty()
            .usingRecursiveComparison()
            .isEqualTo(Optional.of(expected));
    }

    @DisplayName("должен загружать список комментариев для книги")
    @Test
    void shouldFindCommentListByBookId() {
        var actualComments = repository.findAllCommentsByBookId(1L);
        assertThat(actualComments)
            .hasSize(2)
            .map(Comment::getContent)
            .contains("Comment 1_1", "Comment 1_2");
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteCommentById() {
        repository.deleteById(1L);
        var comment = em.find(Comment.class, 1L);
        assertThat(comment).isNull();
    }

    @DisplayName("должен добавить комментарий")
    @Test
    void shouldInsertComment() {
        var book = bookRepository.findById(1L).get();
        var comment = new Comment(0, "New test comment", book);
        repository.save(comment);

        var actualComments = repository.findAllCommentsByBookId(1L);
        assertThat(actualComments)
            .hasSize(3)
            .map(Comment::getContent)
            .contains("Comment 1_1", "Comment 1_2", "New test comment");
    }

    @DisplayName("должен обновить комментарий")
    @Test
    void shouldUpdateComment() {
        var comment = repository.findById(1L).get();
        comment.setContent("Updated comment content");
        repository.save(comment);

        var actualComment = em.find(Comment.class, 1L);
        assertThat(actualComment)
            .isNotNull()
            .hasFieldOrPropertyWithValue("content", "Updated comment content");
    }

    @DisplayName("должен вернуть исключение при обновлении комментария с неизвестной книгой")
    @Test
    void shouldThrowThenUpdateCommentWithUnknownBook() {
        assertThrows(NoSuchElementException.class, () -> {
            var comment = new Comment(100L, "New test comment", bookRepository.findById(100L).get());
            comment.setContent("Updated comment content");
            repository.save(comment);
        });
    }

}