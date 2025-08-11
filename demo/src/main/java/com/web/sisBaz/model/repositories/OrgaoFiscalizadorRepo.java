package com.web.sisBaz.model.repositories;

import com.web.sisBaz.model.entities.OrgaoFiscalizador;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrgaoFiscalizadorRepo {

    public void salvar(OrgaoFiscalizador orgao) throws SQLException {
        if (orgao.getId() == 0) {
            inserir(orgao);
        } else {
            atualizar(orgao);
        }
    }

    private void inserir(OrgaoFiscalizador orgao) throws SQLException {
        String sql = "INSERT INTO orgao_fiscalizador (nome, descricao) VALUES (?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, orgao.getNome());
            stmt.setString(2, orgao.getDescricao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                orgao.setId(rs.getInt(1));
            }
        }
    }

    public OrgaoFiscalizador buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM orgao_fiscalizador WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                OrgaoFiscalizador orgao = new OrgaoFiscalizador();
                orgao.setId(rs.getInt("id"));
                orgao.setNome(rs.getString("nome"));
                orgao.setDescricao(rs.getString("descricao"));
                return orgao;
            }
            return null;
        }
    }

    public List<OrgaoFiscalizador> listarTodos() throws SQLException {
        List<OrgaoFiscalizador> lista = new ArrayList<>();
        String sql = "SELECT * FROM orgao_fiscalizador ORDER BY id ASC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrgaoFiscalizador orgao = new OrgaoFiscalizador();
                orgao.setId(rs.getInt("id"));
                orgao.setNome(rs.getString("nome"));
                orgao.setDescricao(rs.getString("descricao"));
                lista.add(orgao);
            }
        }
        return lista;
    }

    public void atualizar(OrgaoFiscalizador orgao) throws SQLException {
        String sql = "UPDATE orgao_fiscalizador SET nome = ?, descricao = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orgao.getNome());
            stmt.setString(2, orgao.getDescricao());
            stmt.setInt(3, orgao.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM orgao_fiscalizador WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<OrgaoFiscalizador> buscarPorNome(String nome) throws SQLException {
        List<OrgaoFiscalizador> lista = new ArrayList<>();
        String sql = "SELECT * FROM orgao_fiscalizador WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrgaoFiscalizador orgao = new OrgaoFiscalizador();
                orgao.setId(rs.getInt("id"));
                orgao.setNome(rs.getString("nome"));
                orgao.setDescricao(rs.getString("descricao"));
                lista.add(orgao);
            }
        }
        return lista;
    }
}