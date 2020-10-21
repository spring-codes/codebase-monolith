CREATE MEMORY TABLE `authority` (`name` VARCHAR(50) PRIMARY KEY);

CREATE MEMORY TABLE `user` (
`id` SERIAL PRIMARY KEY,
`login` VARCHAR (50) NOT NULL ,
`password_hash` VARCHAR (60) NOT NULL,
`first_name` VARCHAR(50),
`last_name` VARCHAR(50),
`email` VARCHAR (255) UNIQUE,
`activated` BOOLEAN DEFAULT FALSE ,
`lang_key` VARCHAR (10) not null,
`image_url` VARCHAR (255),
`activation_key` VARCHAR (20),
`reset_key` VARCHAR (20),
`reset_date` TIMESTAMP WITH TIME ZONE NOT NULL);