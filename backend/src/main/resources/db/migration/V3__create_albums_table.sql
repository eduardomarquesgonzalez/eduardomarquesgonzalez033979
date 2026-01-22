-- V3__create_albums_table.sql
-- Tabela de álbuns musicais

CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    release_date DATE,
    number_of_tracks INTEGER,
    description TEXT,
    artist_id BIGINT NOT NULL,
    CONSTRAINT fk_albums_artist FOREIGN KEY (artist_id) 
        REFERENCES artists(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX idx_albums_title ON albums(title);
CREATE INDEX idx_albums_artist_id ON albums(artist_id);
CREATE INDEX idx_albums_release_date ON albums(release_date);

-- Comentários para documentação
COMMENT ON TABLE albums IS 'Tabela de álbuns musicais';
COMMENT ON COLUMN albums.title IS 'Título do álbum';
COMMENT ON COLUMN albums.release_date IS 'Data de lançamento do álbum';
COMMENT ON COLUMN albums.number_of_tracks IS 'Número de faixas do álbum';
COMMENT ON COLUMN albums.description IS 'Descrição detalhada do álbum';
COMMENT ON COLUMN albums.artist_id IS 'Referência ao artista dono do álbum';
