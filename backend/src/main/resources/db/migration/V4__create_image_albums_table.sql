-- V4__create_image_albums_table.sql
-- Tabela de imagens dos álbuns

CREATE TABLE image_albums (
    id BIGSERIAL PRIMARY KEY,
    object_key VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    album_id BIGINT NOT NULL,
    CONSTRAINT fk_image_albums_album FOREIGN KEY (album_id) 
        REFERENCES albums(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX idx_image_albums_album_id ON image_albums(album_id);
CREATE INDEX idx_image_albums_object_key ON image_albums(object_key);

-- Comentários para documentação
COMMENT ON TABLE image_albums IS 'Tabela de imagens associadas aos álbuns';
COMMENT ON COLUMN image_albums.object_key IS 'Chave do objeto no storage (S3/MinIO)';
COMMENT ON COLUMN image_albums.content_type IS 'Tipo MIME da imagem (ex: image/jpeg, image/png)';
COMMENT ON COLUMN image_albums.album_id IS 'Referência ao álbum';
