create table `Role` (
	roleID SMALLINT NOT NULL,
    name varchar(64) NOT NULL,
    PRIMARY KEY (roleID)
    );

insert into Role(roleID, name) VALUES (1, 'Admin');
insert into Role(roleID, name) VALUES (2, 'Employee');

create table `User` (
	userID SMALLINT NOT NULL,
    email varchar(320) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    roleID SMALLINT NOT NULL,
    PRIMARY KEY (userID),
    FOREIGN KEY (roleID) REFERENCES Role(roleID)
);

create table Token (
	userID SMALLINT NOT NULL,
    token varchar(64) NOT NULL,
    expiry DATETIME NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID)
);