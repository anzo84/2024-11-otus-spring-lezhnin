create table receipts (
    id bigserial,
    datetime timestamp not null,
    primary key (id)
);

create table positions (
    id bigserial,
    product_name varchar(255) not null,
    uom varchar(50) not null,
    quantity decimal(19,2) not null,
    price decimal(19,2) not null,
    receipt_id bigint references receipts (id) on delete cascade
);


