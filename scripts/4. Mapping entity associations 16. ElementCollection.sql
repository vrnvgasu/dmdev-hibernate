create table company_locale
(
    company_id int not null references company(id),
    lang char(3) not null,
    description varchar(128) not null,
    primary key (company_id, lang)
)