INSERT INTO accounts_test.users (username, first_name, second_name, email, phone_number)
VALUES ('testusername', 'Test', 'Testov', 'test@example.com', '+1 (555) 123-4567') on conflict (id) do nothing;