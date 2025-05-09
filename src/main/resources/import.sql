
SET @userUuid = RANDOM_UUID();
INSERT INTO users(user_uuid, name, username, email, password, active) values(@userUuid, 'tomas', 'tgv', 'tgv@perico.com', '12345678', 'ACTIVE');

SET @phoneUuid = RANDOM_UUID();
INSERT INTO PHONES (phone_uuid, country_code, city_code, phone_number, user_uuid) values(@phoneUuid,  '56', '032', '123456789', @userUuid);

SET @phoneUuid = RANDOM_UUID();
INSERT INTO PHONES (phone_uuid, country_code, city_code, phone_number, user_uuid) values(@phoneUuid,  '56', '033', '123456780', @userUuid); 