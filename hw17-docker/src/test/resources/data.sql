insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(content, book_id)
values ( 'Comment 1_1', 1 ),
       ( 'Comment 1_2', 1 ),
       ( 'Comment 2_1', 2 );

insert into users(username, password) values ( 'admin', '123456' );
insert into roles(user_id, role_alias) values ( 1, 'ROLE_ADMINISTRATOR' );
