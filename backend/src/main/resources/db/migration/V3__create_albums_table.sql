-- V3__create_albums_table.sql
CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL
);

CREATE INDEX idx_albums_title ON albums(title);
