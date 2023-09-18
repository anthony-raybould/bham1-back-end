CREATE TABLE `Role` (
	roleID SMALLINT NOT NULL,
    name   varchar(64) NOT NULL,
    PRIMARY KEY (roleID)
);

INSERT INTO Role(roleID, name) VALUES (1, 'Admin');
INSERT INTO Role(roleID, name) VALUES (2, 'Employee');

CREATE TABLE `User` (
	userID   SMALLINT NOT NULL AUTO_INCREMENT,
    email    varchar(320) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    roleID   SMALLINT NOT NULL,
    PRIMARY KEY (userID),
    UNIQUE (email),
    FOREIGN KEY (roleID) REFERENCES Role(roleID)
);

CREATE TABLE Token (
	userID SMALLINT NOT NULL,
    token  varchar(64) NOT NULL,
    expiry DATETIME NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID)
);

CREATE TABLE JobRole (
    jobRoleID SMALLINT NOT NULL AUTO_INCREMENT,
    name      VARCHAR(64) NOT NULL,
    PRIMARY KEY (jobRoleID),
    UNIQUE (name)
);
