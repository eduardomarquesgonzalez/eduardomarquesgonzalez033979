-- V5__create_regional_table.sql
-- Tabela de regionais (sem auto-increment)

CREATE TABLE regional (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Índices para melhorar performance
CREATE INDEX idx_regional_nome ON regional(nome);
CREATE INDEX idx_regional_ativo ON regional(ativo);

-- Comentários para documentação
COMMENT ON TABLE regional IS 'Tabela de regionais administrativas';
COMMENT ON COLUMN regional.id IS 'ID manual da regional (não auto-incrementado)';
COMMENT ON COLUMN regional.nome IS 'Nome da regional';
COMMENT ON COLUMN regional.ativo IS 'Indica se a regional está ativa';
