-- V2__create_artists_table.sql
-- Tabela de artistas musicais

CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(100)
);

-- Índices para melhorar performance
CREATE INDEX idx_artists_name ON artists(name);
CREATE INDEX idx_artists_type ON artists(type);

-- Comentários para documentação
COMMENT ON TABLE artists IS 'Tabela de artistas musicais';
COMMENT ON COLUMN artists.name IS 'Nome do artista ou banda';
COMMENT ON COLUMN artists.type IS 'Tipo do artista (ex: Solo, Banda, Duo)';
