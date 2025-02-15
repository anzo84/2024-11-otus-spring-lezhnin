package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Comment {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String content;

    @DBRef(lazy = true)
    private Book book;

    public Comment(Book book) {
        this.book = book;
    }
}