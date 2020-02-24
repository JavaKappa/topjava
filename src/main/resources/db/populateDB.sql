DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals(description, calories, user_id)
VALUES ('user_Meal 1', 500, 100000),
       ('user_Meal 2', 750, 100000),
       ('admin_Meal 1', 500, 100001),
       ('admin_Meal 2', 123, 100001);

INSERT INTO meals (description, date_time, calories, user_id) VALUES ('meal in other day', '12.02.2008', 200, 100001)