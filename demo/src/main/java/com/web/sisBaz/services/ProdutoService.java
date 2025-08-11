package com.web.sisBaz.services;

import com.web.sisBaz.model.entities.Produto;
import com.web.sisBaz.model.repositories.ProdutoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepo produtoRepo;
    
    public List<Produto> listarTodos() {
        try {
            return produtoRepo.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os produtos", e);
        }
    }
    
    public Produto buscarPorCodigo(int codigo) {
        try {
            return produtoRepo.buscarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por código: " + codigo, e);
        }
    }
    
    public void salvar(Produto produto) {
        try {
            produtoRepo.salvar(produto);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto", e);
        }
    }
    
    public void atualizar(Produto produto) {
        try {
            produtoRepo.atualizar(produto);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }
    
    public void deletar(int codigo) {
        try {
            produtoRepo.deletar(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto", e);
        }
    }
    
    public List<Produto> buscarPorNome(String nome) {
        try {
            return produtoRepo.buscarPorNome(nome);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por nome", e);
        }
    }
    
    public List<Produto> buscarPorLote(int loteId) {
        try {
            return produtoRepo.buscarPorLote(loteId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por lote", e);
        }
    }
}