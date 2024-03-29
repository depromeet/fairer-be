CREATE TABLE team
(
    team_id BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (team_id)
);

CREATE TABLE member
(
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    team_id BIGINT NULL,
    email VARCHAR(50) NOT NULL,
    profile_path VARCHAR(50) NULL,
    social_type VARCHAR(50) NOT NULL,
    member_name VARCHAR(50) NOT NULL,
    password VARCHAR(300) NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE assignment
(
    assignment_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    housework_id BIGINT NOT NULL,
    PRIMARY KEY (assignment_id)
);

CREATE TABLE housework
(
    housework_id BIGINT NOT NULL AUTO_INCREMENT,
    space_name VARCHAR(30) NOT NULL,
    housework_name VARCHAR(50) NOT NULL,
    scheduled_date DATE NOT NULL,
    scheduled_time TIME NOT NULL,
    success_datetime DATETIME NULL,
    success BOOLEAN NOT NULL,
    PRIMARY KEY (housework_id)
);
