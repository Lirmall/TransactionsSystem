CREATE TABLE IF NOT EXISTS transactions.types
(
    id bigserial NOT NULL,
    name character varying(256) NOT NULL,
    PRIMARY KEY (id)
    );