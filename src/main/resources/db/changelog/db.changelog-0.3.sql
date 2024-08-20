--liquibase formatted sql

--changeset sdsd:1
alter table main_bill
    add column is_frozen boolean not null default false
