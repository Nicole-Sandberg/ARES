CREATE TABLE customer (
	id int primary key, 
	time_zone varchar(50)
);
CREATE TABLE account (
	id int primary key, 
	uname varchar(50), 
	passwd varchar(50), 
	language int, 
	customer int
);