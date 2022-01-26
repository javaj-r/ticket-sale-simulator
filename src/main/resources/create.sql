-- DROP DATABASE IF EXISTS cinema;

/*
CREATE DATABASE cinema
    WITH
    ENCODING = 'UTF8';
*/

-- CREATE SCHEMA IF NOT EXISTS public;

-- SET SEARCH_PATH TO cinema, public;

-- DROP TABLE IF EXISTS USERS;

CREATE TABLE IF NOT EXISTS USERS
(
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(100),
    enabled     BOOLEAN,
    email       VARCHAR(100)
);

-- DROP TABLE IF EXISTS PRIVILEGE;

CREATE TABLE IF NOT EXISTS PRIVILEGE
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50) UNIQUE
);

-- DROP TABLE IF EXISTS USERS_PRIVILEGE;

CREATE TABLE IF NOT EXISTS USERS_PRIVILEGE
(
    user_id     INTEGER,
    privilege_id     INTEGER,
    PRIMARY KEY (user_id, privilege_id),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

-- DROP TABLE IF EXISTS CUSTOMER;

CREATE TABLE IF NOT EXISTS CUSTOMER
(
    id              SERIAL PRIMARY KEY,
    firstname       VARCHAR(100),
    lastname        VARCHAR(100),
    national_code   BIGINT,
    mobile_number   BIGINT,
    user_id         INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id)
);

-- DROP TABLE IF EXISTS CINEMA;

CREATE TABLE IF NOT EXISTS CINEMA
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(100),
    balance BIGINT,
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id)
);

-- DROP TABLE IF EXISTS TICKET;

CREATE TABLE IF NOT EXISTS TICKET
(
    id          SERIAL PRIMARY KEY,
    movie_name  VARCHAR(100),
    show_date   DATE,
    show_time   TIME,
    number      INTEGER,
    sold        INTEGER,
    price       BIGINT,
    user_id   INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id)
);

-- DROP TABLE IF EXISTS USERS_TICKET;

CREATE TABLE IF NOT EXISTS USERS_TICKET
(
    id   SERIAL PRIMARY KEY,
    user_id INTEGER,
    ticket_id   INTEGER,
--     PRIMARY KEY (user_id, customer_id),
    FOREIGN KEY (user_id) REFERENCES USERS (id),
    FOREIGN KEY (ticket_id) REFERENCES TICKET (id)
);
