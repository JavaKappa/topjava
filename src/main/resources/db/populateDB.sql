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

INSERT INTO meals(description, date_time, calories, user_id)
VALUES ('user_Meal 1', '21.04.2020 7:20', 500, 100000),
       ('user_Meal 2', '21.04.2020 12:34', 750, 100000),
       ('user_Meal 3', '23.04.2020 17:30', 1750, 100000),
       ('admin_Meal 1', '21.04.2020 5:10', 500, 100001),
       ('admin_Meal 2', '21.04.2020 14:20', 123, 100001);
