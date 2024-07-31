--liqudbase formatted sql

--changeset sdsd:1
create table users
(
    id                bigserial primary key,
    name              varchar(48)        not null,
    surname           varchar(48)        not null,
    patronymic        varchar(48),
    email             varchar(48) unique not null,
    password          varchar(128)       not null,
    registration_date timestamp          not null,
    is_active         boolean            not null
);

--changeset sdsd:2
create table main_bill
(
    id        bigserial primary key,
    open_date timestamp        not null,
    user_id   bigint
        references users (id)
            on delete cascade  not null,
    balance   numeric
        check ( balance >= 0 ) not null
);

--changeset sdsd:3
create table money_transaction
(
    id               bigserial primary key,
    type             varchar(20)
        check ( type = 'replenishment' or type = 'withdrawal') not null,
    transaction_date timestamp                                 not null,
    amount           numeric check ( amount > 0 ),
    description      text,
    main_bill_id     bigint
        references main_bill (id)
            on delete cascade                                  not null
);

--changeset sdsd:4

create table saving_bill
(
    id            bigserial primary key,
    open_date     timestamp        not null,
    user_id       bigint
        references users (id)
            on delete cascade      not null,
    balance       numeric
        check ( balance >= 0 )     not null,
    target_amount numeric
        check ( target_amount > 0) not null,
    description   text
);