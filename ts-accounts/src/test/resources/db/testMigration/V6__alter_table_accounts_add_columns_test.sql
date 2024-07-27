alter table bank_accounts
add column blocked boolean default false,
add column deleted boolean default false;