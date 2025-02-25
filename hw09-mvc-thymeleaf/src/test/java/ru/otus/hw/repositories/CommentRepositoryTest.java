package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.persistence.model.CommentEntity;
import ru.otus.hw.persistence.repository.BookRepository;
import ru.otus.hw.persistence.repository.CommentRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен найти комментарий по id")
    @Test
    void shouldFindCommentById() {
        long id = 1L;
        var expected = testEntityManager.find(CommentEntity.class, id);
        var actual = repository.findById(id);
        assertThat(actual)
            .isNotEmpty()
            .usingRecursiveComparison()
            .isEqualTo(Optional.of(expected));
    }

    @DisplayName("должен загружать список комментариев для книги")
    @Test
    void shouldFindCommentListByBookId() {
        var actualComments = repository.findByBookId(1L);
        assertThat(actualComments)
            .hasSize(2)
            .map(CommentEntity::getContent)
            .contains("Comment 1_1", "Comment 1_2");
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteCommentById() {
        repository.deleteById(1L);
        var comment = testEntityManager.find(CommentEntity.class, 1L);
        assertThat(comment).isNull();
    }

    @DisplayName("должен добавить комментарий")
    @Test
    void shouldInsertComment() {
        var book = bookRepository.findById(1L).orElseThrow();
        var comment = new CommentEntity(1L, "New test comment", book);
        repository.save(comment);

        var actualComments = repository.findByBookId(1L);
        assertThat(actualComments)
            .hasSize(3)
            .map(CommentEntity::getContent)
            .contains("Comment 1_1", "Comment 1_2", "New test comment");
    }

    @DisplayName("должен обновить комментарий")
    @Test
    void shouldUpdateComment() {
        var comment = repository.findById(1L).orElseThrow();
        comment.setContent("Updated comment content");
        repository.save(comment);

        var actualComment = testEntityManager.find(CommentEntity.class, 1L);
        assertThat(actualComment)
            .isNotNull()
            .hasFieldOrPropertyWithValue("content", "Updated comment content");
    }

    @DisplayName("должен вернуть исключение при обновлении комментария с неизвестной книгой")
    @Test
    void shouldThrowThenUpdateCommentWithUnknownBook() {
        assertThrows(NoSuchElementException.class, () -> {
            var comment = new CommentEntity(100L, "New test comment", bookRepository.findById(100L).get());
            comment.setContent("Updated comment content");
            repository.save(comment);
        });
    }

}