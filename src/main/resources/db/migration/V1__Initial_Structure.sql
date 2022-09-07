CREATE TABLE authors
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

ALTER SEQUENCE authors_id_seq RESTART WITH 1;

CREATE TABLE ted_talks
(
    id        BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES authors (id),
    name      TEXT   NOT NULL,
    date      DATE   NOT NULL,
    views     BIGINT NOT NULL,
    likes     BIGINT NOT NULL,
    link      TEXT   NOT NULL,
    UNIQUE (author_id, name)
);

ALTER SEQUENCE ted_talks_id_seq RESTART WITH 1;
