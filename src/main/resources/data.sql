/*Login roles*/
INSERT INTO roles (id_role, name) VALUES (default, 'ROLE_ADMIN');
INSERT INTO roles (id_role, name) VALUES (default, 'ROLE_USER');
 
 /*Users*/
INSERT INTO users(id_user, username, password) VALUES (default, 'admin', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO');
INSERT INTO users(id_user, username, password) VALUES (default, 'user', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO');

/*Connecting roles and users*/
INSERT INTO role_users (role_id, user_id) VALUES (1, 1);
INSERT INTO role_users (role_id, user_id) VALUES (2, 2);

/*Profile default*/
INSERT INTO profiles(id_profile) VALUES(default);
INSERT INTO profiles(id_profile) VALUES(default);

 /*Images*/
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'beach.jpg', 'Florida Beach');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'turtle.jpg', 'Big Turtle Under the Sea');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'big-tree.jpg', 'Giant Old Tree');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'mountains-1.jpg', 'Snowy Japanese Mountains');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'road.jpg', 'Highway to Paradice');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'mountains-2.jpg', 'Serenity and Peace');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'stars.jpg', 'Endless Space');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'forest-2.jpg', 'The Autumn Leaves');
 INSERT INTO images(id_image, image_name, image_title) VALUES(default, 'mountains-3.jpg', 'Just Green and Beautiful');

  /*Images-Profiles*/
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 1);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 2);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 3);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 4);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 5);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 6);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 7);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 8);
  INSERT INTO images_profiles (profile_id, image_id) VALUES (2, 9);