create table chat
(
  id bigserial primary key,
  name varchar(64) not null unique
);

create table users_chat
(
  user_id bigint references users(id),
  chat_id bigint references chat(id),
  primary key (user_id, chat_id)
);