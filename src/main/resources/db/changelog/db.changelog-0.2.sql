--liqudbase formatted sql

--changeset sdsd:1
create table email_token (
    id bigserial primary key,
    token text not null unique,
    expire_date timestamp not null,
    email text not null
 )