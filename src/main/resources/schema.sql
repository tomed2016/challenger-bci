create table users (
	user_uuid uuid not null default random_uuid(), 
	email varchar(100) not null, 
	name varchar(50) not null,
	username varchar(12) not null UNIQUE,	
	password varchar(60) not null, 
	active enum ('ACTIVE','INACTIVE'), 
	created_at timestamp(6), 
	last_login timestamp(6), 
	updated_at timestamp(6), 
	primary key (user_uuid));

create table phones (
	phone_uuid uuid not null default random_uuid(), 
	city_code varchar(3) not null, 
	contry_code varchar(3) not null,
	phone_number varchar(12) not null, 
	primary key (phone_uuid));
