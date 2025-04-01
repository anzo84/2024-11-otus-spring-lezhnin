package ru.otus.hw.persistence.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("books")
public class BookEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("title")
    private String title;

    @Column("author_id")
    private Long authorId;

    @Transient
    private AuthorEntity author;

    @Transient
    private Set<GenreEntity> genres = new HashSet<>();
}