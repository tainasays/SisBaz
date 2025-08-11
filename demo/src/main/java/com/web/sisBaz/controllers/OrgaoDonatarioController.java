package com.web.sisBaz.controllers;

import java.sql.SQLException;
import java.util.List;

import com.web.sisBaz.model.entities.OrgaoDonatario;
import com.web.sisBaz.model.repositories.OrgaoDonatarioRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orgaos-donatarios")
public class OrgaoDonatarioController {

    private OrgaoDonatarioRepo repository = new OrgaoDonatarioRepo();

    @GetMapping
    public String listar(Model model) throws SQLException {
        model.addAttribute("lista", repository.listarTodos());
        return "orgao_donatario/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute OrgaoDonatario orgao) throws SQLException {
        repository.salvar(orgao);
        return "redirect:/orgaos-donatarios";
    }

    @GetMapping("/buscarPorNome")
    public String buscarPorNome(@RequestParam String nome, Model model) throws SQLException {
        model.addAttribute("lista", repository.buscarPorNome(nome));
        return "orgao_donatario/lista";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("orgaoDonatario", new OrgaoDonatario());
        return "orgao_donatario/form";
    }

    @GetMapping("/editar")
    public String editar(@RequestParam int id, Model model) throws SQLException {
        OrgaoDonatario orgao = repository.buscarPorId(id);
        model.addAttribute("orgaoDonatario", orgao);
        return "orgao_donatario/form";
    }

    @GetMapping("/deletar")
    public String deletar(@RequestParam int id) throws SQLException {
        repository.deletar(id);
        return "redirect:/orgaos-donatarios";
    }
}