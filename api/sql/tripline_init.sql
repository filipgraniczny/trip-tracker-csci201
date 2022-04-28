USE heroku_efbc5c1a3000eab;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Trip;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS Photo;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE User (
                      id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      pfp_id INT
);

CREATE TABLE Trip (
                      id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                      title VARCHAR(255),
                      description VARCHAR(255),
                      location_id INT,
                      from_time timestamp NOT NULL,
                      to_time timestamp,
                      author_id INT
);

CREATE TABLE Event (
                       id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       category VARCHAR(255), -- change to enum --
                       description VARCHAR(255),
                       location_id INT,
                       from_time timestamp NOT NULL,
                       to_time timestamp,
                       trip_id INT
);

CREATE TABLE Location (
                          id INT PRIMARY KEY  NOT NULL auto_increment,
                          name VARCHAR(255) UNIQUE NOT NULL,
                          longitude float not null,
                          latitude float not null
);

CREATE TABLE Photo (
                       id INT PRIMARY KEY NOT NULL auto_increment,
                       trip_id INT,
                       event_id INT,
                       caption varchar(255),
                       object_key_aws varchar(255) not null
);

insert into location values (1, "Pizza Hut", 36.310964264913835, -95.6181994369886);
insert into location values (2, "Fils house", 42.19926134458096, -87.94301347820314);
insert into location values (3, "Chicago", 41.88277533619124, -87.62236465365434);
insert into location values (4, "Vietnam", 12.597957668941982, 108.37575639536017);

insert into event values (1, "Party at Fils", "rager", "dope rager at fils house. best night of my life", 2, NOW(), NOW(), 1);
insert into event values (2, "mountain tour", "tour", "hiking and fun in vietnam, had lot of good food", 4, NOW(), NOW(), 2);
insert into event values (3, "Pan Pizza Tournament", "food", "who can eat the most slices of pan pizza in 30 mins? sponsored by dominos", 1, NOW(), NOW(), 3);
insert into event values (4, "Bean there, done that", "exploring", "checking out chicagos amazing... bean, erm, sky gate  ;_;", 3, NOW(), NOW(), 4);


insert into trip values (1, "visiting fil", "what happens at fils stays at fils ;) ;)", 2, NOW(), NOW(), 1);
insert into trip values (2, "vietnam", "awesome adventures in vietnam", 4, NOW(), NOW(), 1);
insert into trip values (3, "pizza tour", "less get some pizza!!", 1, NOW(), NOW(), 1);
insert into trip values (4, "chicago squad", "fil and ariana vibe in chicago", 3, NOW(), NOW(), 1);

insert into user values (1, "Tommy Ytipline", "tommy@tripline.com", "ttt", 1);

insert into photo values (1, 2, 1, "fil's basement", "aws_poggers");

ALTER TABLE Photo ADD FOREIGN KEY (trip_id) REFERENCES Trip(id);
ALTER TABLE Photo ADD FOREIGN KEY (event_id) REFERENCES Event(id);
ALTER TABLE User ADD FOREIGN KEY (pfp_id) REFERENCES Photo(id);
ALTER TABLE Trip ADD FOREIGN KEY (author_id) REFERENCES User(id);
ALTER TABLE Trip ADD FOREIGN KEY (location_id) REFERENCES Location(id);
ALTER TABLE Event ADD FOREIGN KEY (location_id) REFERENCES Location(id);
ALTER TABLE Event ADD FOREIGN KEY (trip_id) REFERENCES Trip(id);