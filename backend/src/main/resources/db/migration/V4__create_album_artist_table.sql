-- V4__create_album_artist_table.sql
CREATE TABLE album_artist (
    album_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    PRIMARY KEY (album_id, artist_id),
    CONSTRAINT fk_album_artist_album FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    CONSTRAINT fk_album_artist_artist FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
);

CREATE INDEX idx_album_artist_album_id ON album_artist(album_id);
CREATE INDEX idx_album_artist_artist_id ON album_artist(artist_id);
