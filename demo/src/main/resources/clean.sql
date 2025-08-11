DELETE FROM lote_produto;
DELETE FROM lote;
DELETE FROM orgao_fiscalizador;
DELETE FROM orgao_donatario;
DELETE FROM produto;


ALTER SEQUENCE IF EXISTS produto_codigo_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS orgao_donatario_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS orgao_fiscalizador_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS lote_id_seq RESTART WITH 1;
