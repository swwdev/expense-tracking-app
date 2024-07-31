--liqudbase formatted sql

--changeset sdsd:1

alter table valid_refresh_token
    add column finger_print text not null default 'dummy'