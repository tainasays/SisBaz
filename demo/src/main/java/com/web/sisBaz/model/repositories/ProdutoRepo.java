package com.web.sisBaz.model.repositories;

import com.web.sisBaz.model.entities.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ProdutoRepo {

    public void salvar(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao) VALUES (?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                produto.setCodigo(rs.getInt(1));
            }
        }
    }

    public Produto buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM produto WHERE codigo = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                return produto;
            }
            return null;
        }
    }
    
    
    public List<Produto> buscarPorNome(String nome) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE nome = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
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

    public List<Produto> buscarPorLote(int loteId) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.* FROM produto p " +
                     "INNER JOIN lote_produto lp ON p.codigo = lp.produto_codigo " +
                     "WHERE lp.lote_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY codigo ASC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                lista.add(produto);
            }
        }
        return lista;
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ? WHERE codigo = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getCodigo());
            stmt.executeUpdate();
        }
    }

    public void deletar(int codigo) throws SQLException {
        String sqlDeleteLoteProduto = "DELETE FROM lote_produto WHERE produto_codigo = ?";
        String sqlDeleteProduto = "DELETE FROM produto WHERE codigo = ?";

        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(sqlDeleteLoteProduto);
                 PreparedStatement stmt2 = conn.prepareStatement(sqlDeleteProduto)) {

                stmt1.setInt(1, codigo);
                stmt1.executeUpdate();

                stmt2.setInt(1, codigo);
                stmt2.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback(); 
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }


    public List<Produto> listarDisponiveis() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM produto p " +
                     "LEFT JOIN lote_produto lp ON p.codigo = lp.produto_codigo " +
                     "WHERE lp.produto_codigo IS NULL " +
                     "ORDER BY p.codigo ASC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                lista.add(produto);
            }
        }
        return lista;
    }

 
    public boolean isProdutoDisponivel(int codigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lote_produto WHERE produto_codigo = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) == 0; 
            }
            return true;
        }
    }

 
    public Integer buscarLoteVinculado(int codigo) throws SQLException {
        String sql = "SELECT lote_id FROM lote_produto WHERE produto_codigo = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("lote_id");
            }
            return null; 
        }
    }
}
