package com.web.sisBaz.services;

import com.web.sisBaz.model.entities.Lote;
import com.web.sisBaz.model.repositories.LoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class LoteService {
    
    @Autowired
    private LoteRepo loteRepo;
    
    public List<Lote> listarTodos() {
        try {
            return loteRepo.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os lotes", e);
        }
    }
    
    public Lote buscarPorId(int id) {
        try {
            return loteRepo.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lote por ID: " + id, e);
        }
    }
    
    public void salvar(Lote lote) {
        try {
            loteRepo.salvar(lote);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar lote", e);
        }
    }
    
    public void deletar(int id) {
        try {
            loteRepo.deletar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar lote com ID: " + id, e);
        }
    }
    
    public List<Lote> buscarPorFiscalizador(int fiscalizadorId) {
        try {
            return loteRepo.buscarPorFiscalizador(fiscalizadorId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lotes por fiscalizador", e);
        }
    }
    
    public List<Lote> buscarPorDonatario(int donatarioId) {
        try {
            return loteRepo.buscarPorDonatario(donatarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lotes por donatário", e);
        }
    }
}