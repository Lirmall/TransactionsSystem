insert into transactions.types (id, "name") values (1, 'Payment') on conflict do nothing;
insert into transactions.types (id, "name") values (2, 'Deposit') on conflict do nothing;
insert into transactions.types (id, "name") values (3, 'Cash Withdrawal') on conflict do nothing;
insert into transactions.types (id, "name") values (4, 'Transfer') on conflict do nothing;
