MERGE INTO customer(id, time_zone) VALUES(1, 'Europe/Oslo');
MERGE INTO account(id, uname, passwd, language, customer) VALUES(1, 'testUser', 'testPassword', 113, 1);
