SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS trip;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS photo;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE user (
                      id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      pfp_id INT
);

CREATE TABLE trip (
                      id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                      title VARCHAR(255),
                      description VARCHAR(255),
                      location_id INT,
                      from_time timestamp NOT NULL,
                      to_time timestamp,
                      author_id INT
);

CREATE TABLE event (
                       id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       category VARCHAR(255), -- change to enum --
                       description VARCHAR(255),
                       location_id INT,
                       from_time timestamp NOT NULL,
                       to_time timestamp,
                       trip_id INT
);

CREATE TABLE location (
                          id INT PRIMARY KEY  NOT NULL auto_increment,
                          name VARCHAR(255) UNIQUE NOT NULL,
                          longitude float not null,
                          latitude float not null
);

CREATE TABLE photo (
                       id INT PRIMARY KEY NOT NULL auto_increment,
                       trip_id INT,
                       event_id INT,
                       caption varchar(255),
                       object_key_aws varchar(255) not null
);

ALTER TABLE photo ADD FOREIGN KEY (trip_id) REFERENCES trip(id);
ALTER TABLE photo ADD FOREIGN KEY (event_id) REFERENCES event(id);
ALTER TABLE user ADD FOREIGN KEY (pfp_id) REFERENCES photo(id);
ALTER TABLE trip ADD FOREIGN KEY (author_id) REFERENCES user(id);
ALTER TABLE trip ADD FOREIGN KEY (location_id) REFERENCES location(id);
ALTER TABLE event ADD FOREIGN KEY (location_id) REFERENCES location(id);
ALTER TABLE event ADD FOREIGN KEY (trip_id) REFERENCES trip(id);
