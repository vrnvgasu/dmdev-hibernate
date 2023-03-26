drop table if exists profile;

create table profile
(
    id bigserial primary key,
    user_id bigint not null unique references users(id),
    street varchar(128),
    language char(3)
);