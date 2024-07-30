INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (2, 'twoaccsuser', 'Twoacc', 'Twoaccovich', 'Twoaccov', 'twoacc@example.com', '+1 (222) 234-5678', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (3, 'blockeduser', 'Block', 'Blockovic', 'Blockov', 'blocked@example.com', '+2 (333) 345-6789', true, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (4, 'deleteduser', 'Del', 'Delovich', 'Delov', 'deleted@example.com', '+2 (444) 456-7890', false, true) on conflict (id) do nothing;
INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (5, 'threeaccsuser', 'Threeacc', 'Threeaccovich', 'Threeaccov', 'threeacc@example.com', '+3 (333) 567-8901', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (6, 'fordeluser', 'Fordel', 'Fordelovich', 'Fordelov', 'fordelc@example.com', '+4 (123) 123-1212', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (7, 'forblockuser', 'Forblock', 'Forblockovich', 'Forblockov', 'forblock@example.com', '+5 (121) 231-5634', false, false) on conflict (id) do nothing;
INSERT INTO accounts_test.users (id, username, first_name, third_name, second_name, email, phone_number, blocked, deleted)
VALUES (8, 'forupdateuser', 'Forupdate', 'Forupdatovich', 'Forupdatokov', 'forupdate@example.com', '+6 (541) 513-6548', false, false) on conflict (id) do nothing;