INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('twoaccsuser', 'Twoacc', 'Twoaccovich', 'Twoaccov', 'twoacc@example.com', '+1 (222) 234-5678', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('blockeduser', 'Block', 'Blockovic', 'Blockov', 'blocked@example.com', '+2 (333) 345-6789', true, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('deleteduser', 'Del', 'Delovich', 'Delov', 'deleted@example.com', '+2 (444) 456-7890', false, true) on conflict (id) do nothing;
INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('threeaccsuser', 'Threeacc', 'Threeaccovich', 'Threeaccov', 'threeacc@example.com', '+3 (333) 567-8901', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('fordeluser', 'Fordel', 'Fordelovich', 'Fordelov', 'fordelc@example.com', '+4 (123) 123-1212', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('forblockuser', 'Forblock', 'Forblockovich', 'Forblockov', 'forblock@example.com', '+5 (121) 231-5634', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES ('forupdateuser', 'Forupdate', 'Forupdatovich', 'Forupdatokov', 'forupdate@example.com', '+6 (541) 513-6548', false, false) on conflict (id) do nothing;