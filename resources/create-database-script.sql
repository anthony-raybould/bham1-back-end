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

CREATE TABLE `JobCapability` (
    capabilityID SMALLINT NOT NULL AUTO_INCREMENT,
    capabilityName VARCHAR(64) NOT NULL,
    PRIMARY KEY (capabilityID)
);

insert into JobCapability(capabilityID, capabilityName) VALUES (1, 'Engineering');
insert into JobCapability(capabilityID, capabilityName) VALUES (2, 'Cyber  Security');
insert into JobCapability(capabilityID, capabilityName) VALUES (3, 'Data & AI');


CREATE TABLE `JobBands` (
    bandID SMALLINT NOT NULL AUTO_INCREMENT,
    bandName VARCHAR(64) NOT NULL,
    PRIMARY KEY (bandID)
);

insert into JobBands(bandID, bandName) VALUES (7, 'Apprentice');
insert into JobBands(bandID, bandName) VALUES (6, 'Trainee');
insert into JobBands(bandID, bandName) VALUES (5, 'Associate');
insert into JobBands(bandID, bandName) VALUES (4, 'Senior Associate');
insert into JobBands(bandID, bandName) VALUES (3, 'Consultant');
insert into JobBands(bandID, bandName) VALUES (2, 'Manager');
insert into JobBands(bandID, bandName) VALUES (1, 'Principal');




CREATE TABLE `JobRoles` (
    jobRoleID SMALLINT NOT NULL AUTO_INCREMENT,
    jobRoleName VARCHAR(64) NOT NULL,
    jobSpecSummary TEXT,
    bandID SMALLINT NOT NULL,
    capabilityID SMALLINT NOT NULL,
    responsibilities TEXT,
    sharePoint VARCHAR(255),
    PRIMARY KEY (jobRoleID),
    FOREIGN KEY (bandID) REFERENCES JobBands(bandID),
    FOREIGN KEY (capabilityID) REFERENCES JobCapability(capabilityID)
);

insert into JobRoles(jobRoleID, jobRoleName,jobSpecSummary,bandID,capabilityID,responsibilities,sharePoint)
 VALUES (1, 'Trainee Software Engineer', "As a Trainee Software Engineer with Kainos, you will work on projects where you can make a real difference to people’s lives – the lives of people you know. After taking part in our award-winning, seven-week Engineering Academy, you will then join one of our many project teams, to learn from our experienced developers, project managers and customer-facing staff. You’ll have great support and mentoring, balanced with the experience of being given real, meaningful work to do, to help you truly develop both technically and professionally.",
 1 ,2,"Contribute to developing high quality solutions which impact the lives of users worldwide. You’ll work as part of a team to solve problems and produce innovative software solutions. Learn about new technologies and approaches, with talented colleagues who will help you learn, develop and grow. Based in our Kainos office and often on our customer sites, you will work on project teams to learn how to develop and unit test straightforward or low complexity components, and then moving on to more complex elements as you increase your knowledge. Work with other developers in working through designs and user stories and to produce real development solutions. Will be fully supported by experienced colleagues in the team to follow designs, and then progress to assist in any other aspect of the project life-cycle under supervision. Develop excellent technical, team-working, and Agile project experience.", "https://kainossoftwareltd.sharepoint.com/:b:/r/people/Job%20Specifications/Engineering/Job%20profile%20-%20Software%20Engineer%20(Trainee).pdf?csf=1&web=1&e=nQzHld");
insert into JobRoles(jobRoleID, jobRoleName,jobSpecSummary,bandID,capabilityID,responsibilities,sharePoint)
 VALUES (2, 'Associate Software Engineer',
 "Your responsibilities include actively participating in real projects, learning development languages, and technologies, with support from experienced colleagues and mentors. You'll demonstrate a passion for technology, strong teamwork skills, and creative problem-solving abilities, contributing to innovative solutions and approaches within Kainos.",
 3 ,2,
 "As an Apprentice Software Engineer at Kainos, you'll contribute to meaningful projects, receive comprehensive training, and collaborate within tight-knit teams, all while exploring diverse development technologies and finding your niche in Software Engineering.", "https://kainossoftwareltd.sharepoint.com/:b:/r/people/Job%20Specifications/Engineering/Job%20profile%20-%20Software%20Engineer%20(Associate).pdf?csf=1&web=1&e=GHWpmX");
