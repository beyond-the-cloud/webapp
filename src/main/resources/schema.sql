create table if not exists users
(
	username varchar(50) not null
		primary key,
	password varchar(70) not null,
	enabled tinyint(1) not null
);

create table if not exists user
(
	id char(40) null,
	firstName varchar(20) null,
	lastName varchar(20) null,
	emailAddress char(50) null,
	password char(70) null,
	accountCreated char(50) null,
	accountUpdated char(30) null
);

create table if not exists authorities
(
	username varchar(50) not null,
	authority varchar(50) not null
);