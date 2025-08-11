package com.web.sisBaz.model.repositories;

import com.web.sisBaz.model.entities.OrgaoDonatario;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrgaoDonatarioRepo {

    public void salvar(OrgaoDonatario orgao) throws SQLException {
        System.out.println("Salvando órgão donatário: " + orgao.getNome());
        if (orgao.getId() == 0) {
            System.out.println("Inserindo novo órgão");
            inserir(orgao);
        } else {
            System.out.println("Atualizando órgão existente, ID: " + orgao.getId());
            atualizar(orgao);
        }
        System.out.println("Salvamento concluído");
    }

    private void inserir(OrgaoDonatario orgao) throws SQLException {
        String sql = "INSERT INTO orgao_donatario (nome, endereco, telefone, horario_funcionamento, descricao) VALUES (?, ?, ?, ?, ?)";
        System.out.println("DEBUG: Executando SQL: " + sql);
        System.out.println("DEBUG: Valores - Nome: " + orgao.getNome() + ", Endereco: " + orgao.getEndereco()); 
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, orgao.getNome());
            stmt.setString(2, orgao.getEndereco());
            stmt.setString(3, orgao.getTelefone());
            stmt.setString(4, orgao.getHorarioFuncionamento());
            stmt.setString(5, orgao.getDescricao());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Linhas afetadas: " + rowsAffected);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                orgao.setId(newId);
                System.out.println("ID gerado: " + newId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir: " + e.getMessage());
            throw e;
        }
    }

    public OrgaoDonatario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM orgao_donatario WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                OrgaoDonatario orgao = new OrgaoDonatario();
                orgao.setId(rs.getInt("id"));
                orgao.setNome(rs.getString("nome"));
                orgao.setEndereco(rs.getString("endereco"));
                orgao.setTelefone(rs.getString("telefone"));
                orgao.setHorarioFuncionamento(rs.getString("horario_funcionamento"));
                orgao.setDescricao(rs.getString("descricao"));
                return orgao;
            }
            return null;
        }
    }

    public List<OrgaoDonatario> listarTodos() throws SQLException {
        List<OrgaoDonatario> lista = new ArrayList<>();
        String sql = "SELECT * FROM orgao_donatario ORDER BY id ASC";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrgaoDonatario orgao = new OrgaoDonatario();
                orgao.setId(rs.getInt("id"));
                orgao.setNome(rs.getString("nome"));
                orgao.setEndereco(rs.getString("endereco"));
                orgao.setTelefone(rs.getString("telefone"));
                orgao.setHorarioFuncionamento(rs.getString("horario_funcionamento"));
                orgao.setDescricao(rs.getString("descricao"));
                lista.add(orgao);
            }
        }
        return lista;
    }

    public void atualizar(OrgaoDonatario orgao) throws SQLException {
        String sql = "UPDATE orgao_donatario SET nome = ?, endereco = ?, telefone = ?, horario_funcionamento = ?, descricao = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orgao.getNome());
            stmt.setString(2, orgao.getEndereco());
            stmt.setString(3, orgao.getTelefone());
            stmt.setString(4, orgao.getHorarioFuncionamento());
            stmt.setString(5, orgao.getDescricao());
            stmt.setInt(6, orgao.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM orgao_donatario WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<OrgaoDonatario> buscarPorNome(String nome) throws SQLException {
        List<OrgaoDonatario> lista = new ArrayList<>();
        String sql = "SELECT * FROM orgao_donatario WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrgaoDonatario orgao = new OrgaoDonatario();
                orgao.setId(rs.getInt("id"));
                orgao.setNome(rs.getString("nome"));
                orgao.setEndereco(rs.getString("endereco"));
                orgao.setTelefone(rs.getString("telefone"));
                orgao.setHorarioFuncionamento(rs.getString("horario_funcionamento"));
                orgao.setDescricao(rs.getString("descricao"));
                lista.add(orgao);
            }
        }
        return lista;
    }
}