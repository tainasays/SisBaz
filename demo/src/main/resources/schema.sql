CREATE SCHEMA IF NOT EXISTS sisbaz;

SET search_path TO sisbaz, public;

CREATE TABLE IF NOT EXISTS sisbaz.produto (
    codigo SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS sisbaz.orgao_donatario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    telefone VARCHAR(50),
    horario_funcionamento VARCHAR(100),
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS sisbaz.orgao_fiscalizador (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS sisbaz.lote (
    id SERIAL PRIMARY KEY,
    data_entrega DATE,
    observacao TEXT,
    orgao_donatario_id INT REFERENCES sisbaz.orgao_donatario(id),
    orgao_fiscalizador_id INT REFERENCES sisbaz.orgao_fiscalizador(id)
);

CREATE TABLE IF NOT EXISTS sisbaz.lote_produto (
    lote_id INT REFERENCES sisbaz.lote(id),
    produto_codigo INT REFERENCES sisbaz.produto(codigo),
    PRIMARY KEY (lote_id, produto_codigo),
    UNIQUE (produto_codigo) 
);
