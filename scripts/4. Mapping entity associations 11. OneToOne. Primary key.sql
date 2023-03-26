create table profile
(
  user_id bigint primary key references users(id),
  street varchar(128),
  language char(3)
);