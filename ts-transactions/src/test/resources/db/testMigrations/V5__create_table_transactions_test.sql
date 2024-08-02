CREATE TABLE IF NOT EXISTS transactions_test.transactions
(
    id uuid NOT NULL default uuid_generate_v4(),
    sender_id bigint NOT NULL,
    recipient_id bigint NOT NULL,
    amount double precision NOT NULL,
    type bigint NOT NULL,
    status bigint NOT NULL,
    transaction_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (type) REFERENCES transactions_test.types (id) ON DELETE NO ACTION,
    FOREIGN KEY (status) REFERENCES transactions_test.statuses (id) ON DELETE NO ACTION
    );