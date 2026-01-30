-- V2__create_artists_table.sql
CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE
);

CREATE INDEX idx_artists_name_lower ON artists (LOWER(name));
