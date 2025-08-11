-- Dados de teste para o SisBaz
-- Este arquivo é executado após o schema.sql
-- Versão idempotente: só insere dados se as tabelas estiverem vazias

-- Definir o schema atual
SET search_path TO sisbaz, public;


INSERT INTO sisbaz.produto (nome, descricao) 
SELECT 'Veículo', 'Fiat Uno'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.produto WHERE nome = 'Veículo');

INSERT INTO sisbaz.produto (nome, descricao) 
SELECT 'Moto', 'Moto vermelha'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.produto WHERE nome = 'Moto');

INSERT INTO sisbaz.produto (nome, descricao) 
SELECT 'Notebook', 'Notebool Dell'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.produto WHERE nome = 'Notebook');


INSERT INTO sisbaz.orgao_donatario (nome, endereco, telefone, horario_funcionamento, descricao) 
SELECT 'Prefeitura Municipal', 'Rua Central, 123', '(11) 1234-5678', '08:00-17:00', 'Prefeitura da cidade'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.orgao_donatario WHERE nome = 'Prefeitura Municipal');

INSERT INTO sisbaz.orgao_donatario (nome, endereco, telefone, horario_funcionamento, descricao) 
SELECT 'Secretaria de Assistência Social', 'Av. Principal, 456', '(11) 2345-6789', '07:00-16:00', 'Órgão de assistência social'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.orgao_donatario WHERE nome = 'Secretaria de Assistência Social');

-- Inserir órgãos fiscalizadores de teste (apenas se não existirem)
INSERT INTO sisbaz.orgao_fiscalizador (nome, descricao) 
SELECT 'Controladoria Geral', 'Órgão responsável pela fiscalização das doações'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.orgao_fiscalizador WHERE nome = 'Controladoria Geral');

INSERT INTO sisbaz.orgao_fiscalizador (nome, descricao) 
SELECT 'Ministério Público', 'Fiscalização judicial das doações'
WHERE NOT EXISTS (SELECT 1 FROM sisbaz.orgao_fiscalizador WHERE nome = 'Ministério Público');
