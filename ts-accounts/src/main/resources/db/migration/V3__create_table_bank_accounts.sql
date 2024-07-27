CREATE TABLE IF NOT EXISTS accounts.bank_accounts
(
    id bigserial NOT NULL,
    owner_user bigint NOT NULL,
    balance double precision NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_owner_user FOREIGN KEY (owner_user)
        REFERENCES accounts.users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);