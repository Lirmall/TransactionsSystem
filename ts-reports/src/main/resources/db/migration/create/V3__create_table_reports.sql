CREATE TABLE IF NOT EXISTS reports.reports
(
    id uuid NOT NULL,
    sender_user_id bigint NOT NULL,
    sender_user_username character varying(32) NOT NULL,
    sender_bank_account_id bigint NOT NULL,
    amount double precision NOT NULL,
    recipient_user_id bigint NOT NULL,
    recipient_user_username character varying(32) NOT NULL,
    recipient_bank_account_id bigint NOT NULL,
    type_id bigint NOT NULL,
    type_name character varying(32) NOT NULL,
    status_id bigint NOT NULL,
    status_name character varying(32) NOT NULL,
    transaction_date timestamp without time zone NOT NULL,
    PRIMARY KEY (id)
);