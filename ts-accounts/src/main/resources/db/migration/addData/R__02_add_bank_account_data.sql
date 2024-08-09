INSERT INTO accounts.bank_accounts (id, owner_user, balance)
values (1, 1, 100000.0) on conflict (id) do nothing;
INSERT INTO accounts.bank_accounts (id, owner_user, balance)
values (2, 1, 10.0) on conflict (id) do nothing;