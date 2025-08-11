package com.web.sisBaz.controllers;

import java.sql.SQLException;
import com.web.sisBaz.model.entities.OrgaoFiscalizador;
import com.web.sisBaz.model.repositories.OrgaoFiscalizadorRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orgaos-fiscalizadores")
public class OrgaoFiscalizadorController {

    private OrgaoFiscalizadorRepo repository = new OrgaoFiscalizadorRepo();

    @GetMapping
    public String listar(Model model) throws SQLException {
        model.addAttribute("lista", repository.listarTodos());
        return "orgao_fiscalizador/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute OrgaoFiscalizador orgao) throws SQLException {
        repository.salvar(orgao);
        return "redirect:/orgaos-fiscalizadores";
    }

    @GetMapping("/buscarPorNome")
    public String buscarPorNome(@RequestParam String nome, Model model) throws SQLException {
        model.addAttribute("lista", repository.buscarPorNome(nome));
        return "orgao_fiscalizador/lista";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("orgaoFiscalizador", new OrgaoFiscalizador());
        return "orgao_fiscalizador/form";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam int id, Model model) throws SQLException {
        OrgaoFiscalizador orgao = repository.buscarPorId(id);
        model.addAttribute("orgaoFiscalizador", orgao);
        return "orgao_fiscalizador/form";
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam int id) throws SQLException {
        repository.deletar(id);
        return "redirect:/orgaos-fiscalizadores";
    }
}