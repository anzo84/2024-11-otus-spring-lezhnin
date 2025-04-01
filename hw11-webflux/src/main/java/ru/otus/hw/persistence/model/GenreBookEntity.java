package ru.otus.hw.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("books_genres")
public class GenreBookEntity {

    @Transient
    private Long id;// Суррогатный ключ

    @Column("book_id")
    private Long bookId;

    @Column("genre_id")
    private Long genreId;
}
