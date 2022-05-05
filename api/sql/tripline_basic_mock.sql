SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE user;
TRUNCATE TABLE trip;
TRUNCATE TABLE event;
TRUNCATE TABLE location;
TRUNCATE TABLE photo;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO location (id, name, longitude, latitude) VALUES (1, 'Pizza Hut', 36.31, -95.61);
INSERT INTO location (id, name, longitude, latitude) VALUES (2, 'Fils house', 42.19, -87.94);
INSERT INTO location (id, name, longitude, latitude) VALUES (3, 'Chicago', 41.88, -87.62);
INSERT INTO location (id, name, longitude, latitude) VALUES (4, 'Vietnam', 12.59, 108.37);

INSERT INTO user (id, name, email, password, pfp_id) VALUES (1, 'Tommy Ytipline', 'tommy@tripline.com', 'ttt', null);

INSERT INTO trip (id, title, description, location_id, from_time, to_time, author_id) values (1, 'visiting fil', 'what happens at fils stays at fils ;) ;)', 2, NOW(), NOW(), 1);
INSERT INTO trip (id, title, description, location_id, from_time, to_time, author_id)  values (2, 'vietnam', 'awesome adventures in vietnam', 4, NOW(), NOW(), 1);
INSERT INTO trip (id, title, description, location_id, from_time, to_time, author_id)  values (3, 'pizza tour', 'less get some pizza!!', 1, NOW(), NOW(), 1);
INSERT INTO trip (id, title, description, location_id, from_time, to_time, author_id)  values (4, 'chicago squad', 'fil and ariana vibe in chicago', 3, NOW(), NOW(), 1);


INSERT INTO event (id, name, category, description, location_id ,from_time, to_time, trip_id) values (1, 'Party at Fils', 'rager', "dope rager at fils house. best night of my life", 2, NOW(), NOW(), 1);
INSERT INTO event (id, name, category, description, location_id ,from_time, to_time, trip_id) values (2, 'mountain tour', 'tour', "hiking and fun in vietnam, had lot of good food", 4, NOW(), NOW(), 2);
INSERT INTO event (id, name, category, description, location_id ,from_time, to_time, trip_id) values (3, 'Pan Pizza Tournament', 'food', "who can eat the most slices of pan pizza in 30 mins? sponsored by dominos", 1, NOW(), NOW(), 3);
INSERT INTO event (id, name, category, description, location_id ,from_time, to_time, trip_id) values (4, 'Bean there, done that', 'exploring', "checking out chicagos amazing... bean, erm, sky gate  ;_;", 3, NOW(), NOW(), 4);

INSERT INTO photo (id, trip_id, event_id, caption, object_key_aws) values (1, 2, 1, "fil's basement", 'aws_poggers');
