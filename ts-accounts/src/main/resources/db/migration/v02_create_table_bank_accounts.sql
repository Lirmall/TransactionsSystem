CREATE SEQUENCE IF NOT EXISTS accounts.bank_accounts_sequence
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS accounts.bank_accounts
(
    id bigint NOT NULL,
    owner_user bigint NOT NULL,
    balance double precision NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_owner_user FOREIGN KEY (owner_user)
        REFERENCES accounts.users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);