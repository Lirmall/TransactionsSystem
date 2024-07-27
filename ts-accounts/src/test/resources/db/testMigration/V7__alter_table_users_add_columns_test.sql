alter table users
add column blocked boolean default false,
add column deleted boolean default false;