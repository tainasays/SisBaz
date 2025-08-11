package com.web.sisBaz.services;

import com.web.sisBaz.model.entities.OrgaoFiscalizador;
import com.web.sisBaz.model.repositories.OrgaoFiscalizadorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrgaoFiscalizadorService {
    
    @Autowired
    private OrgaoFiscalizadorRepo orgaoFiscalizadorRepo;
    
    public List<OrgaoFiscalizador> listarTodos() {
        try {
            return orgaoFiscalizadorRepo.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os órgãos fiscalizadores", e);
        }
    }
    
    public OrgaoFiscalizador buscarPorId(int id) {
        try {
            return orgaoFiscalizadorRepo.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar órgão fiscalizador por ID: " + id, e);
        }
    }
    
    public void salvar(OrgaoFiscalizador orgaoFiscalizador) {
        try {
            orgaoFiscalizadorRepo.salvar(orgaoFiscalizador);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar órgão fiscalizador", e);
        }
    }
    
    public void atualizar(OrgaoFiscalizador orgaoFiscalizador) {
        try {
            orgaoFiscalizadorRepo.atualizar(orgaoFiscalizador);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar órgão fiscalizador", e);
        }
    }
    
    public void deletar(int id) {
        try {
            orgaoFiscalizadorRepo.deletar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar órgão fiscalizador", e);
        }
    }
    
    public List<OrgaoFiscalizador> buscarPorNome(String nome) {
        try {
            return orgaoFiscalizadorRepo.buscarPorNome(nome);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar órgão fiscalizador por nome", e);
        }
    }
}