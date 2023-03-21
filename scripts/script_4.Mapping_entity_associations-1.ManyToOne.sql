drop table if exists users;
drop sequence if exists users_id_seq;

create table if not exists company
(
    id serial primary key,
    name varchar(64) not null unique
);
create table users
(
    id bigserial primary key,
    firstname  varchar(128),
    lastname   varchar(128),
    birth_date date,
    username   varchar(128) unique,
    role       varchar(32),
    info       jsonb,
    company_id int references company(id)
);