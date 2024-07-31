--liqudbase formatted sql

--changeset sdsd:1
create table valid_refresh_token
(
    id bigserial primary key,
    token text unique not null
);