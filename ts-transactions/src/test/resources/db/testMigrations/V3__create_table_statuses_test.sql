CREATE TABLE IF NOT EXISTS transactions.statuses
(
    id bigserial NOT NULL,
    name character varying(256) NOT NULL,
    PRIMARY KEY (id)
    );