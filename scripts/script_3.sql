drop table if exists users;
drop sequence if exists users_id_seq;

create table users
(
    -- BIGSERIAL - bigint автогенерируемый
    -- id bigint primary key, можно вместо BIGSERIAL создать sequence руками
    id BIGSERIAL primary key,
    username   varchar(128) unique,
    firstname  varchar(128),
    lastname   varchar(128),
    birth_date date,
    role       varchar(32),
    info       jsonb
);

-- create sequence users_id_seq owned by users.id;

-- костыль, чтобы показать стратегию TABLE, когда в БД нет автоинкрементов и последовательностей
-- drop table if exists users;
-- drop sequence if exists users_id_seq;
-- create table users
-- (
--     id bigint,
--     username   varchar(128) unique,
--     firstname  varchar(128),
--     lastname   varchar(128),
--     birth_date date,
--     role       varchar(32),
--     info       jsonb
-- );
-- create table if not exists all_sequence
-- (
--     table_name varchar(32) primary key,
--     pk_value BIGINT not null
-- )