INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (2, 2, 2000.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (3, 2, 12000.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (4, 3, 1000.0, true, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (5, 4, 5000.0, false, true) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (6, 5, 15000.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (7, 5, 35000.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (8, 5, 105000.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (9, 6, 120.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (10, 6, 20.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (11, 7, 1220.0, false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.bank_accounts (id, owner_user, balance, blocked, deleted)
values (12, 7, 11220.0, false, false) on conflict (id) do nothing;