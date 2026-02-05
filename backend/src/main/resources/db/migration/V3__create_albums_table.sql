-- V3__create_albums_table.sql
CREATE TABLE albums (
     id BIGSERIAL PRIMARY KEY,
     title VARCHAR(200) NOT NULL,
     cover_url VARCHAR(500)
 );

 CREATE INDEX idx_albums_title ON albums(title);

 CREATE INDEX idx_albums_title_lower ON albums(LOWER(title));
