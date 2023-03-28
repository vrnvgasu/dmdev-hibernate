drop table if exists users_chat;

create table users_chat
(
    id bigserial primary key,
    user_id bigint references users(id),
    chat_id bigint references chat(id),
    created_at timestamp not null,
    created_by varchar(128) not null
);