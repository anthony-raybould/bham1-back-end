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

CREATE TABLE `Capability` (
    capabilityID SMALLINT NOT NULL AUTO_INCREMENT,
    capabilityName VARCHAR(64) NOT NULL,
    PRIMARY KEY (capabilityID)
);

insert into Capability(capabilityID, capabilityName) VALUES (1, 'Engineering');
insert into Capability(capabilityID, capabilityName) VALUES (2, 'Cyber  Security');
insert into Capability(capabilityID, capabilityName) VALUES (3, 'Data & AI');


CREATE TABLE `Bands` (
    bandID SMALLINT NOT NULL AUTO_INCREMENT,
    bandName VARCHAR(64) NOT NULL,
    PRIMARY KEY (bandID)
);

insert into Bands(bandID, bandName) VALUES (1, 'Apprentice');
insert into Bands(bandID, bandName) VALUES (2, 'Trainee');
insert into Bands(bandID, bandName) VALUES (3, 'Associate');
insert into Bands(bandID, bandName) VALUES (4, 'Senior Associate');


CREATE TABLE `Roles` (
    jobRoleID SMALLINT NOT NULL AUTO_INCREMENT,
    jobRoleName VARCHAR(64) NOT NULL,
    jobSpecSummary TEXT,
    bandID SMALLINT NOT NULL,
    capabilityID SMALLINT NOT NULL,
    responsibilities TEXT,
    sharePoint VARCHAR(255),
    PRIMARY KEY (jobRoleID),
    FOREIGN KEY (bandID) REFERENCES JobBand(bandID),
    FOREIGN KEY (capabilityID) REFERENCES JobCapability(capabilityID)
);

insert into Roles(jobRoleID, jobRoleName,jobSpecSummary,bandID,capabilityID,responsibilities,sharePoint)
 VALUES (1, 'Trainee Software Engineer', "Lorem ipsum super long words",1, ,2"lorem impsum some more", "https://kainossoftwareltd.sharepoint.com/:b:/r/people/Job%20Specifications/Engineering/Job%20profile%20-%20Software%20Engineer%20(Trainee).pdf?csf=1&web=1&e=nQzHld");
insert into Roles(jobRoleID, jobRoleName,jobSpecSummary,bandID,capabilityID,responsibilities,sharePoint)
 VALUES (2, 'Associate Software Engineer', "Lorem ipsum super long words",3, ,2"lorem impsum some more", "https://kainossoftwareltd.sharepoint.com/:b:/r/people/Job%20Specifications/Engineering/Job%20profile%20-%20Software%20Engineer%20(Associate).pdf?csf=1&web=1&e=GHWpmX");


