package com.web.sisBaz.services;

import com.web.sisBaz.model.entities.OrgaoDonatario;
import com.web.sisBaz.model.repositories.OrgaoDonatarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrgaoDonatarioService {
    
    @Autowired
    private OrgaoDonatarioRepo orgaoDonatarioRepo;
    
    public List<OrgaoDonatario> listarTodos() {
        try {
            return orgaoDonatarioRepo.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os órgãos donatários", e);
        }
    }
    
    public OrgaoDonatario buscarPorId(int id) {
        try {
            return orgaoDonatarioRepo.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar órgão donatário por ID: " + id, e);
        }
    }
    
    public void salvar(OrgaoDonatario orgaoDonatario) {
        try {
            orgaoDonatarioRepo.salvar(orgaoDonatario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar órgão donatário", e);
        }
    }
    
    public void atualizar(OrgaoDonatario orgaoDonatario) {
        try {
            orgaoDonatarioRepo.atualizar(orgaoDonatario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar órgão donatário", e);
        }
    }
    
    public void deletar(int id) {
        try {
            orgaoDonatarioRepo.deletar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar órgão donatário", e);
        }
    }
    
    public List<OrgaoDonatario> buscarPorNome(String nome) {
        try {
            return orgaoDonatarioRepo.buscarPorNome(nome);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar órgão donatário por nome", e);
        }
    }
}