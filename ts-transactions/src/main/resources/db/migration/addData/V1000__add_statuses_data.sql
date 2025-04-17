insert into transactions.statuses (id, "name") values (1, 'Success') on conflict do nothing;
insert into transactions.statuses (id, "name") values (2, 'In Progress') on conflict do nothing;
insert into transactions.statuses (id, "name") values (3, 'Failed') on conflict do nothing;
insert into transactions.statuses (id, "name") values (4, 'Cancelled') on conflict do nothing;
insert into transactions.statuses (id, "name") values (5, 'Pending Confirmation') on conflict do nothing;
