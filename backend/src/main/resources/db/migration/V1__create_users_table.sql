-- V1__create_users_table.sql
-- Tabela de usuários com suporte ao Spring Security

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar performance nas buscas
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_enabled ON users(enabled);

-- Comentários para documentação
COMMENT ON TABLE users IS 'Tabela de usuários do sistema com autenticação';
COMMENT ON COLUMN users.role IS 'Roles possíveis: USER, ADMIN';
COMMENT ON COLUMN users.enabled IS 'Indica se o usuário está ativo no sistema';
