create database javafx_app;
use javafx_app;

create table users
(
    user_id    INT NOT NULL AUTO_INCREMENT,
    username   VARCHAR(45),
    password   VARCHAR(45),
    favChannel VARCHAR(45),
    PRIMARY KEY (user_id)
);