--liqudbase formatted sql

--changeset sdsd:1
alter table valid_refresh_token
    add column email text not null default 'dummy'