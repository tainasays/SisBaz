package com.web.sisBaz.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.web.sisBaz.model.entities.Lote;
import com.web.sisBaz.model.entities.Produto;
import com.web.sisBaz.model.repositories.LoteRepo;
import com.web.sisBaz.model.repositories.OrgaoDonatarioRepo;
import com.web.sisBaz.model.repositories.OrgaoFiscalizadorRepo;
import com.web.sisBaz.model.repositories.ProdutoRepo;

@Controller
@RequestMapping("/lotes")
public class LoteController {

	private LoteRepo repository = new LoteRepo();

	@GetMapping
	public String listar(Model model) throws SQLException {
		model.addAttribute("lista", repository.listarTodos());
		return "lote/lista";
	}

	@PostMapping("/salvar")
	public String salvar(@ModelAttribute Lote lote, 
	                     @RequestParam int orgaoDonatarioId,
	                     @RequestParam int orgaoFiscalizadorId,
	                     @RequestParam String dataEntregaDate,
	                     @RequestParam(required = false) List<Integer> produtosCodigos,
	                     Model model) throws SQLException {
		
		try {
			java.time.LocalDate localDate = java.time.LocalDate.parse(dataEntregaDate);
			long timestamp = localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
			lote.setDataEntrega(timestamp);
		} catch (Exception e) {
			lote.setDataEntrega(System.currentTimeMillis());
		}
		
		OrgaoDonatarioRepo donatarioRepo = new OrgaoDonatarioRepo();
		OrgaoFiscalizadorRepo fiscalizadorRepo = new OrgaoFiscalizadorRepo();
		
		lote.setOrgaoDonatario(donatarioRepo.buscarPorId(orgaoDonatarioId));
		lote.setOrgaoFiscalizador(fiscalizadorRepo.buscarPorId(orgaoFiscalizadorId));

		if (produtosCodigos != null && !produtosCodigos.isEmpty()) {
			List<Produto> produtos = new ArrayList<>();
			ProdutoRepo produtoRepo = new ProdutoRepo();
			List<String> produtosJaVinculados = new ArrayList<>();
			
			for (Integer codigo : produtosCodigos) {
				// Verificar se o produto está disponível
				if (!produtoRepo.isProdutoDisponivel(codigo)) {
					Produto produto = produtoRepo.buscarPorCodigo(codigo);
					Integer loteVinculado = produtoRepo.buscarLoteVinculado(codigo);
					produtosJaVinculados.add(produto.getNome() + " (já vinculado ao lote " + loteVinculado + ")");
				} else {
					Produto produto = produtoRepo.buscarPorCodigo(codigo);
					if (produto != null) {
						produtos.add(produto);
					}
				}
			}

			if (!produtosJaVinculados.isEmpty()) {
				model.addAttribute("erro", "Os seguintes produtos já estão vinculados a outros lotes: " + 
				                           String.join(", ", produtosJaVinculados));
				model.addAttribute("lote", lote);
				
				model.addAttribute("orgaosDonatarios", donatarioRepo.listarTodos());
				model.addAttribute("orgaosFiscalizadores", fiscalizadorRepo.listarTodos());
				model.addAttribute("produtos", produtoRepo.listarDisponiveis());
				
				return "lote/form";
			}
			
			lote.setProdutos(produtos);
		}
		
		repository.salvar(lote);
		return "redirect:/lotes";
	}

	@GetMapping("/buscarPorId")
	public String buscarPorId(@RequestParam int id, Model model) throws SQLException {
		model.addAttribute("lote", repository.buscarPorId(id));
		return "lote/detalhe";
	}

	@GetMapping("/detalhes")
	public String detalhes(@RequestParam int id, Model model) throws SQLException {
		Lote lote = repository.buscarPorId(id);
		model.addAttribute("lote", lote);
		return "lote/detalhes";
	}

	@GetMapping("/filtrar-por-fiscalizador")
	public String filtrarPorFiscalizador(@RequestParam String nomeFiscalizador, Model model) throws SQLException {
		List<Lote> lotesFiltrados = repository.filtrarPorFiscalizador(nomeFiscalizador);
		model.addAttribute("lista", lotesFiltrados);
		model.addAttribute("filtroAtivo", "Filtrado por Fiscalizador: " + nomeFiscalizador);
		model.addAttribute("nomeFiscalizador", nomeFiscalizador); 
		return "lote/lista";
	}

	@GetMapping("/filtrar-por-donatario")
	public String filtrarPorDonatario(@RequestParam String nomeDonatario, Model model) throws SQLException {
		List<Lote> lotesFiltrados = repository.filtrarPorDonatario(nomeDonatario);
		model.addAttribute("lista", lotesFiltrados);
		model.addAttribute("filtroAtivo", "Filtrado por Donatário: " + nomeDonatario);
		model.addAttribute("nomeDonatario", nomeDonatario); 
		return "lote/lista";
	}

	@GetMapping("/form")
	public String form(Model model) throws SQLException {
		model.addAttribute("lote", new Lote());
	
		OrgaoDonatarioRepo donatarioRepo = new OrgaoDonatarioRepo();
		OrgaoFiscalizadorRepo fiscalizadorRepo = new OrgaoFiscalizadorRepo();
		ProdutoRepo produtoRepo = new ProdutoRepo();
		
		model.addAttribute("orgaosDonatarios", donatarioRepo.listarTodos());
		model.addAttribute("orgaosFiscalizadores", fiscalizadorRepo.listarTodos());

		model.addAttribute("produtos", produtoRepo.listarDisponiveis());
		
		return "lote/form";
	}
}
