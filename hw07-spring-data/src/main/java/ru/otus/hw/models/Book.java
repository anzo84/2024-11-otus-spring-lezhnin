package ru.otus.hw.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedEntityGraph(
    name = Book.BOOKS_AUTHOR_GENRES_GRAPH,
    attributeNodes = {
        @NamedAttributeNode("author"),
        @NamedAttributeNode("genres"),
    }
)
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    public static final String BOOKS_AUTHOR_GENRES_GRAPH = "books-author-genres-graph";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
}
