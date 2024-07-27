CREATE TABLE IF NOT EXISTS accounts.users
(
    id bigserial NOT NULL,
    username character varying(256) NOT NULL,
    first_name character varying(256) NOT NULL,
    third_name character varying(256),
    second_name character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    phone_number character varying(256) NOT NULL,
    PRIMARY KEY (id)
    );