CREATE TABLE book
(
    id       UUID PRIMARY KEY,
    title    VARCHAR(64) NOT NULL,
    author   VARCHAR(64) NOT NULL,
    isbn     VARCHAR(17) NOT NULL UNIQUE,
    quantity INT         NOT NULL
);
