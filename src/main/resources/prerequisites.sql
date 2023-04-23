-- Insert all genres
INSERT INTO genre (id, name) VALUES (1, 'Action');
INSERT INTO genre (id, name) VALUES (2, 'Adventure');
INSERT INTO genre (id, name) VALUES (3, 'Animation');
INSERT INTO genre (id, name) VALUES (4, 'Comedy');
INSERT INTO genre (id, name) VALUES (5, 'Crime');
INSERT INTO genre (id, name) VALUES (6, 'Documentary');
INSERT INTO genre (id, name) VALUES (7, 'Drama');
INSERT INTO genre (id, name) VALUES (8, 'Family');
INSERT INTO genre (id, name) VALUES (9, 'Fantasy');
INSERT INTO genre (id, name) VALUES (10, 'History');
INSERT INTO genre (id, name) VALUES (11, 'Horror');
INSERT INTO genre (id, name) VALUES (12, 'Kids');
INSERT INTO genre (id, name) VALUES (13, 'Music');
INSERT INTO genre (id, name) VALUES (14, 'Mystery');
INSERT INTO genre (id, name) VALUES (15, 'News');
INSERT INTO genre (id, name) VALUES (16, 'Politics');
INSERT INTO genre (id, name) VALUES (17, 'Reality');
INSERT INTO genre (id, name) VALUES (18, 'Romance');
INSERT INTO genre (id, name) VALUES (19, 'Science Fiction');
INSERT INTO genre (id, name) VALUES (20, 'Soap');
INSERT INTO genre (id, name) VALUES (21, 'Talk');
INSERT INTO genre (id, name) VALUES (22, 'TV Movie');
INSERT INTO genre (id, name) VALUES (23, 'Thriller');
INSERT INTO genre (id, name) VALUES (24, 'War');
INSERT INTO genre (id, name) VALUES (25, 'Western');

-- Insert all roles
INSERT INTO role (id, name) VALUES (1, 'Admin');
INSERT INTO role (id, name) VALUES (2, 'User');
INSERT INTO role (id, name) VALUES (3, 'Pro User');

-- Insert all permissions
INSERT INTO permission (id, name) VALUES (1, 'Update/Delete Servie Database');
INSERT INTO permission (id, name) VALUES (2, 'Add Servie');
INSERT INTO permission (id, name) VALUES (3, 'Change Servie Poster');
INSERT INTO permission (id, name) VALUES (4, 'Change Servie Backdrop');

-- Insert all role-permission mappings
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 3);
INSERT INTO role_permissions (role_id, permission_id) VALUES (1, 4);

INSERT INTO role_permissions (role_id, permission_id) VALUES (2, 2);

INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 3);
INSERT INTO role_permissions (role_id, permission_id) VALUES (3, 4);





