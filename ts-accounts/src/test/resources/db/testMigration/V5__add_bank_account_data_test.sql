INSERT INTO accounts_test.bank_accounts (owner_user, balance)
values (1, 100000.0) on conflict (id) do nothing;