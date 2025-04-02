package ru.otus.hw.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("comments")
public class CommentEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("content")
    private String content;

    @Column("book_id")
    private Long bookId;

    @Transient
    private BookEntity book;
}