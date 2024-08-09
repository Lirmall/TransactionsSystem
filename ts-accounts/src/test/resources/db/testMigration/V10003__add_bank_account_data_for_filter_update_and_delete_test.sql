INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (2, 2000.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (2, 12000.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (3, 1000.0, true, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (4, 5000.0, false, true) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (5, 15000.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (5, 35000.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (5, 105000.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (6, 120.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (6, 20.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (7, 1220.0, false, false) on conflict (id) do nothing;
INSERT INTO bank_accounts (owner_user, balance, blocked, deleted)
values (7, 11220.0, false, false) on conflict (id) do nothing;