-- V5__create_image_albums_table.sql
CREATE TABLE image_albums (
    id BIGSERIAL PRIMARY KEY,
    object_key VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    album_id BIGINT NOT NULL,
    CONSTRAINT fk_image_albums_album FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE
);

CREATE INDEX idx_image_albums_album_id ON image_albums(album_id);
CREATE INDEX idx_image_albums_object_key ON image_albums(object_key);
