package com.web.sisBaz.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.web.sisBaz.model.entities.Produto;
import com.web.sisBaz.model.repositories.ProdutoRepo;

import java.sql.SQLException;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private ProdutoRepo repository = new ProdutoRepo();

    @GetMapping
    public String listar(Model model) throws SQLException {

        var produtos = repository.listarTodos();
        
        for (Produto produto : produtos) {
            Integer loteVinculado = repository.buscarLoteVinculado(produto.getCodigo());
        }
        
        model.addAttribute("lista", produtos);
        model.addAttribute("repository", repository); 
        return "produto/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) throws SQLException {
        if (produto.getCodigo() > 0) {
          
            repository.atualizar(produto);
        } else {
            
            repository.salvar(produto);
        }
        return "redirect:/produtos";
    }

    @GetMapping("/buscarPorNome")
    public String buscarPorNome(@RequestParam String nome, Model model) throws SQLException {
        model.addAttribute("lista", repository.buscarPorNome(nome));
        return "produto/lista";
    }

    @GetMapping("/buscarPorLote")
    public String buscarPorLote(@RequestParam int loteId, Model model) throws SQLException {
        model.addAttribute("lista", repository.buscarPorLote(loteId));
        return "produto/lista";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/form";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam int codigo, Model model) throws SQLException {
        Produto produto = repository.buscarPorCodigo(codigo);
        model.addAttribute("produto", produto);
        return "produto/form";
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam int codigo) throws SQLException {
        repository.deletar(codigo);
        return "redirect:/produtos";
    }
}