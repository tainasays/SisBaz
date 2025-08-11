package com.web.sisBaz.model.repositories;

import com.web.sisBaz.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class LoteRepo {

    public void salvar(Lote lote) throws SQLException {
        String sql = "INSERT INTO lote (data_entrega, observacao, orgao_donatario_id, orgao_fiscalizador_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Conversão de long para date.
            if (lote.getDataEntrega() > 0) {
                stmt.setDate(1, new java.sql.Date(lote.getDataEntrega()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            stmt.setString(2, lote.getObservacao());
            stmt.setInt(3, lote.getOrgaoDonatario().getId());
            stmt.setInt(4, lote.getOrgaoFiscalizador().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int loteId = rs.getInt(1);
                lote.setId(loteId); // Define o ID no objeto lote
                
                // Registro dos produtos associados ao lote
                if (lote.getProdutos() != null && !lote.getProdutos().isEmpty()) {
                    salvarProdutosDoLote(loteId, lote.getProdutos(), conn);
                }
            }
        }
    }

    private void salvarProdutosDoLote(int loteId, List<Produto> produtos, Connection conn) throws SQLException {
        if (produtos == null || produtos.isEmpty()) {
            return;
        }
        
        String sql = "INSERT INTO lote_produto (lote_id, produto_codigo) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Produto p : produtos) {
                stmt.setInt(1, loteId);
                stmt.setInt(2, p.getCodigo());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public Lote buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT l.id, l.data_entrega, l.observacao, 
                   od.id as donatario_id, od.nome as donatario_nome,
                   of.id as fiscalizador_id, of.nome as fiscalizador_nome
            FROM lote l
            LEFT JOIN orgao_donatario od ON l.orgao_donatario_id = od.id
            LEFT JOIN orgao_fiscalizador of ON l.orgao_fiscalizador_id = of.id
            WHERE l.id = ?
            """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));

                Date sqlDate = rs.getDate("data_entrega");
                lote.setDataEntrega(sqlDate != null ? sqlDate.getTime() : 0L);

                lote.setObservacao(rs.getString("observacao"));
                
                OrgaoDonatario donatario = new OrgaoDonatario();
                donatario.setId(rs.getInt("donatario_id"));
                donatario.setNome(rs.getString("donatario_nome"));
                lote.setOrgaoDonatario(donatario);
                
                OrgaoFiscalizador fiscalizador = new OrgaoFiscalizador();
                fiscalizador.setId(rs.getInt("fiscalizador_id"));
                fiscalizador.setNome(rs.getString("fiscalizador_nome"));
                lote.setOrgaoFiscalizador(fiscalizador);
                
                lote.setProdutos(buscarProdutosDoLote(id, conn));
                
                return lote;
            }
            return null;
        }
    }

    public List<Produto> buscarProdutosDoLote(int loteId, Connection conn) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = """
            SELECT p.codigo, p.nome, p.descricao 
            FROM produto p
            INNER JOIN lote_produto lp ON p.codigo = lp.produto_codigo
            WHERE lp.lote_id = ?
            ORDER BY p.nome
            """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produtos.add(produto);
            }
        }
        return produtos;
    }

    public List<Produto> buscarProdutosDoLote(int loteId) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            return buscarProdutosDoLote(loteId, conn);
        }
    }

    public int contarProdutosDoLote(int loteId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lote_produto WHERE lote_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    
    public List<Lote> buscarPorFiscalizador(int fiscalizadorId) throws SQLException {
        String sql = "SELECT * FROM lote WHERE orgao_fiscalizador_id = ?";
        List<Lote> lotes = new ArrayList<>();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fiscalizadorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));

                Date sqlDate = rs.getDate("data_entrega");
                lote.setDataEntrega(sqlDate != null ? sqlDate.getTime() : 0L);

                lote.setObservacao(rs.getString("observacao"));
                lotes.add(lote);
            }
        }
        return lotes;
    }

    public List<Lote> buscarPorDonatario(int donatarioId) throws SQLException {
        String sql = "SELECT * FROM lote WHERE orgao_donatario_id = ?";
        List<Lote> lotes = new ArrayList<>();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, donatarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));

                Date sqlDate = rs.getDate("data_entrega");
                lote.setDataEntrega(sqlDate != null ? sqlDate.getTime() : 0L);

                lote.setObservacao(rs.getString("observacao"));
                lotes.add(lote);
            }
        }
        return lotes;
    }

    public List<Lote> listarTodos() throws SQLException {
        List<Lote> lotes = new ArrayList<>();
        String sql = """
            SELECT l.id, l.data_entrega, l.observacao, 
                   od.id as donatario_id, od.nome as donatario_nome,
                   of.id as fiscalizador_id, of.nome as fiscalizador_nome
            FROM lote l
            LEFT JOIN orgao_donatario od ON l.orgao_donatario_id = od.id
            LEFT JOIN orgao_fiscalizador of ON l.orgao_fiscalizador_id = of.id
            ORDER BY l.id ASC
            """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));

                Date sqlDate = rs.getDate("data_entrega");
                lote.setDataEntrega(sqlDate != null ? sqlDate.getTime() : 0L);

                lote.setObservacao(rs.getString("observacao"));
                
                OrgaoDonatario donatario = new OrgaoDonatario();
                donatario.setId(rs.getInt("donatario_id"));
                donatario.setNome(rs.getString("donatario_nome"));
                lote.setOrgaoDonatario(donatario);

                OrgaoFiscalizador fiscalizador = new OrgaoFiscalizador();
                fiscalizador.setId(rs.getInt("fiscalizador_id"));
                fiscalizador.setNome(rs.getString("fiscalizador_nome"));
                lote.setOrgaoFiscalizador(fiscalizador);
                
                lote.setProdutos(buscarProdutosDoLote(lote.getId(), conn));
                
                lotes.add(lote);
            }
        }
        return lotes;
    }

    public List<Lote> filtrarPorFiscalizador(String nomeFiscalizador) throws SQLException {
        List<Lote> lotes = new ArrayList<>();
        String sql = """
            SELECT l.id, l.data_entrega, l.observacao, 
                   od.id as donatario_id, od.nome as donatario_nome,
                   of.id as fiscalizador_id, of.nome as fiscalizador_nome
            FROM lote l
            LEFT JOIN orgao_donatario od ON l.orgao_donatario_id = od.id
            LEFT JOIN orgao_fiscalizador of ON l.orgao_fiscalizador_id = of.id
            WHERE UPPER(of.nome) LIKE UPPER(?)
            ORDER BY l.id ASC
            """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeFiscalizador + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));

                Date sqlDate = rs.getDate("data_entrega");
                lote.setDataEntrega(sqlDate != null ? sqlDate.getTime() : 0L);

                lote.setObservacao(rs.getString("observacao"));
                           
                OrgaoDonatario donatario = new OrgaoDonatario();
                donatario.setId(rs.getInt("donatario_id"));
                donatario.setNome(rs.getString("donatario_nome"));
                lote.setOrgaoDonatario(donatario);
                
                OrgaoFiscalizador fiscalizador = new OrgaoFiscalizador();
                fiscalizador.setId(rs.getInt("fiscalizador_id"));
                fiscalizador.setNome(rs.getString("fiscalizador_nome"));
                lote.setOrgaoFiscalizador(fiscalizador);
                
                lote.setProdutos(buscarProdutosDoLote(lote.getId(), conn));
                
                lotes.add(lote);
            }
        }
        return lotes;
    }

    public List<Lote> filtrarPorDonatario(String nomeDonatario) throws SQLException {
        List<Lote> lotes = new ArrayList<>();
        String sql = """
            SELECT l.id, l.data_entrega, l.observacao, 
                   od.id as donatario_id, od.nome as donatario_nome,
                   of.id as fiscalizador_id, of.nome as fiscalizador_nome
            FROM lote l
            LEFT JOIN orgao_donatario od ON l.orgao_donatario_id = od.id
            LEFT JOIN orgao_fiscalizador of ON l.orgao_fiscalizador_id = of.id
            WHERE UPPER(od.nome) LIKE UPPER(?)
            ORDER BY l.id ASC
            """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeDonatario + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));

                Date sqlDate = rs.getDate("data_entrega");
                lote.setDataEntrega(sqlDate != null ? sqlDate.getTime() : 0L);

                lote.setObservacao(rs.getString("observacao"));
                
                OrgaoDonatario donatario = new OrgaoDonatario();
                donatario.setId(rs.getInt("donatario_id"));
                donatario.setNome(rs.getString("donatario_nome"));
                lote.setOrgaoDonatario(donatario);
                
                OrgaoFiscalizador fiscalizador = new OrgaoFiscalizador();
                fiscalizador.setId(rs.getInt("fiscalizador_id"));
                fiscalizador.setNome(rs.getString("fiscalizador_nome"));
                lote.setOrgaoFiscalizador(fiscalizador);
                
                lote.setProdutos(buscarProdutosDoLote(lote.getId(), conn));
                
                lotes.add(lote);
            }
        }
        return lotes;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM lote WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
