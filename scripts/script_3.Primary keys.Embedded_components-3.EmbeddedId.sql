drop table if exists users;
drop sequence if exists users_id_seq;

create table users
(
    firstname  varchar(128) not null ,
    lastname   varchar(128) not null ,
    birth_date date not null ,

    username   varchar(128) unique,
    role       varchar(32),
    info       jsonb,

    primary key (firstname, lastname, birth_date)
);
