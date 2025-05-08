create table users (
	created_at timestamp(6), 
	last_login timestamp(6), 
	updated_at timestamp(6), 
	uuid uuid not null, 
	email varchar(100) not null unique, 
	name varchar(50) not null, 
	password varchar(12) not null, 
	active enum ('ACTIVE','INACTIVE'), 
	primary key (uuid))

create table phones (
	phones_uuid uuid not null, 
	uuid uuid not null, 
	city_code varchar(3) not null, 
	contry_code varchar(3) not null, 
	phone_number varchar(12) not null, primary key (uuid))
