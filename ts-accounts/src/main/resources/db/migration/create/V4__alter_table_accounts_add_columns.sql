alter table bank_accounts
add column blocked boolean not null default false,
add column deleted boolean not null default false;