create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments (
   id bigserial,
   content varchar(255),
   book_id bigint references books (id) on delete cascade,
   primary key (id)
);

create table users (
    id bigserial,
    username varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table roles (
    id bigserial,
    user_id bigint not null references users(id) on delete cascade,
    role_alias varchar(50) not null check (role_alias in ('ROLE_ADMINISTRATOR', 'ROLE_AUTHOR', 'ROLE_COMMENTATOR')),
    primary key (id)
);
