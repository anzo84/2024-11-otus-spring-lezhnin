package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;


import java.util.List;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "000", id = "initDrop", author = "anton.lezhnin@x5.ru", runAlways = true)
    public void initDropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "addGenres", author = "anton.lezhnin@x5.ru")
    public void addGenres(MongoDatabase db) {
        db.getCollection("genres")
            .insertMany(List.of(
                new Document().append("_id", "1").append("name", "Genre-1"),
                new Document().append("_id", "2").append("name", "Genre-2"),
                new Document().append("_id", "3").append("name", "Genre-3")
            ));
    }

    @ChangeSet(order = "002", id = "addAuthors", author = "anton.lezhnin@x5.ru")
    public void addAuthors(MongoDatabase db) {
        db.getCollection("authors")
            .insertMany(List.of(
                new Document().append("_id", "1").append("fullName", "Author-1"),
                new Document().append("_id", "2").append("fullName", "Author-2"),
                new Document().append("_id", "3").append("fullName", "Author-3")
            ));
    }

    @ChangeSet(order = "003", id = "addBooks", author = "anton.lezhnin@x5.ru")
    public void addBooks(BookRepository bookRepository,
                            AuthorRepository authorRepository,
                            GenreRepository genreRepository) {
        Genre genre1 = genreRepository.findById("1").get();
        Genre genre2 = genreRepository.findById("2").get();
        Genre genre3 = genreRepository.findById("3").get();

        bookRepository.insert(List.of(
            new Book("1", "Book-1", authorRepository.findById("2").get(), List.of(genre1, genre2)),
            new Book("2", "Book-2", authorRepository.findById("1").get(), List.of(genre2)),
            new Book("3", "Book-3", authorRepository.findById("3").get(), List.of(genre3))
        ));
    }

    @ChangeSet(order = "004", id = "addComments", author = "anton.lezhnin@x5.ru")
    public void addComments(CommentRepository commentRepository, BookRepository bookRepository) {
        Book book1 = bookRepository.findById("1").get();
        Book book2 = bookRepository.findById("1").get();
        Book book3 = bookRepository.findById("1").get();

        commentRepository.insert(List.of(
            new Comment("1", "Comment-1", book1),
            new Comment("2", "Comment-2", book2),
            new Comment("3", "Comment-3", book3),
            new Comment("4", "Comment-4", book1),
            new Comment("5", "Comment-5", book2),
            new Comment("6", "Comment-6", book3)
        ));
    }
}
