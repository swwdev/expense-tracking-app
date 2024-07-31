-- Insert test data into the user table
INSERT INTO "users" (id, name, surname, patronymic, email, password, registration_date, is_active)
VALUES (1, 'Ivan', 'Ivanov', 'Ivanovich', 'ivan.ivanov@example.com', 'pass123', '2024-01-01 10:00:00', TRUE),
       (2, 'Sidor', 'Sidorov', 'Sidorovich', 'sidor.sidorov@example.com', 'pass123', '2024-01-03 12:00:00', FALSE),
       (3, 'Manyna', 'Pidorov', NULL, 'maua.ppiipiv@example.com', 'pass', '2023-01-03 12:00:00', TRUE),
       (4, 'unconfirmed', 'surrr', 'patrr', 'need.confirm@example.com', 'pass123', now() - interval '1' hour, FALSE);

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM "users"));

-- Insert test data into the main_bill table
INSERT INTO main_bill (id, open_date, user_id, balance, is_frozen)
VALUES (1, '2024-02-01 11:00:00', 1, 0.00, false),
       (2, '2024-01-21 14:12:00', 1, 10.00, true),
       (3, '2024-01-02 12:00:00', 2, 2000.00, true),
       (4, '2024-01-03 13:00:00', 3, 0.00, false),
       (5, '2023-02-02 12:00:00', 2, 1500.00, false),
       (6, '2023-11-12 12:00:00', 3, 1000.24, false);

SELECT SETVAL('main_bill_id_seq', (SELECT MAX(id) FROM main_bill));


-- Insert test data into the saving_bill table
INSERT INTO saving_bill (id, open_date, user_id, balance, target_amount, description)
VALUES (1, '2024-01-01 10:00:00', 1, 1500.00, 10000.00, 'Saving for a new car'),
       (2, '2024-01-12 11:00:00', 2, 1000.00, 20000.00, 'Saving for vacation'),
       (3, '2024-03-02 11:00:00', 1, 3998.00, 20000.00, 'Saving for TV'),
       (4, '2024-02-01 05:12:42', 2, 0.00, 20000.00, 'Saving for vacation'),
       (5, '2024-01-06 05:12:42', 3, 0.00, 32311.00, 'Saving for testosterone course');

SELECT SETVAL('saving_bill_id_seq', (SELECT MAX(id) FROM saving_bill));


-- Insert test data into the money_transaction table
INSERT INTO money_transaction (id, type, transaction_date, amount, description, main_bill_id)
VALUES (1, 'replenishment', '2024-02-01 11:00:00', 500.00, 'Initial deposit', 1),
       (2, 'replenishment', '2024-02-02 11:05:00', 1000.00, 'Salary replenishment', 1),
       (3, 'replenishment', '2024-02-03 12:05:00', 500.00, 'ATM replenishment', 1),
       (4, 'withdrawal', '2024-02-04 12:05:00', 500.00, 'transfer to another bill', 1),
       (5, 'withdrawal', '2024-02-05 10:05:00', 1500.00, 'transfer to saving bill', 1),
       (6, 'replenishment', '2024-01-21 14:12:00', 10.00, 'Initial deposit', 2),
       (7, 'replenishment', '2024-01-02 12:00:00', 500.00, 'Initial deposit', 3),
       (8, 'replenishment', '2024-02-03 12:20:00', 1500.00, 'Salary replenishment', 3),
       (9, 'replenishment', '2024-03-03 12:20:00', 2000.00, 'Salary replenishment', 3),
       (10, 'withdrawal', '2024-02-04 12:20:00', 2000.00, 'Buy new TV', 3),
       (11, 'replenishment', '2024-01-03 13:00:00', 500.00, 'Initial deposit', 4),
       (12, 'replenishment', '2024-02-01 10:15:00', 500.00, 'transfer to another bill', 4),
       (13, 'replenishment', '2024-02-02 10:15:00', 1500.00, 'transfer to another bill', 4),
       (14, 'withdrawal', '2024-02-03 10:15:00', 1500.00, 'transfer to saving bill', 4),
       (15, 'replenishment', '2024-02-04 10:15:00', 500.00, NULL, 4),
       (16, 'withdrawal', '2024-02-12 10:15:00', 1500.00, NULL, 4),
       (17, 'replenishment', '2023-02-02 12:00:00', 1500.00, 'Initial deposit', 5),
       (18, 'withdrawal', '2024-02-03 10:15:00', 500.00, NULL, 5),
       (19, 'replenishment', '2024-02-05 10:15:00', 500.00, NULL, 5),
       (20, 'replenishment', '2023-11-12 12:00:00', 1000.24, 'Initial deposit', 6);

SELECT SETVAL('money_transaction_id_seq', (SELECT MAX(id) FROM money_transaction));

-- Insert test data into the email_token table
INSERT INTO email_token (id, token, expire_date, email)
VALUES (1, 'token1', now() - interval '1' minute, 'ivan.ivanov@example.com'),
       (2, 'token2', now() + interval '1' minute, 'ivan.ivanov@example.com'),
       (3, 'token3', now() + interval '1' year, 'sidor.sidorov@example.com'),
       (4, 'token4', now() - interval '1' year, 'maua.ppiipiv@example.com');

SELECT SETVAL('email_token_id_seq', (SELECT MAX(id) FROM email_token));

