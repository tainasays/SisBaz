package com.web.sisBaz.model.entities;

import java.sql.Date;
import java.util.List;


public class Lote{

    private int id;
    private long dataEntrega;
    private String observacao;
    
    private OrgaoDonatario orgaoDonatario;
    private OrgaoFiscalizador orgaoFiscalizador;
    private List<Produto> produtos;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(long dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public OrgaoDonatario getOrgaoDonatario() {
		return orgaoDonatario;
	}
	public void setOrgaoDonatario(OrgaoDonatario orgaoDonatario) {
		this.orgaoDonatario = orgaoDonatario;
	}
	public OrgaoFiscalizador getOrgaoFiscalizador() {
		return orgaoFiscalizador;
	}
	public void setOrgaoFiscalizador(OrgaoFiscalizador orgaoFiscalizador) {
		this.orgaoFiscalizador = orgaoFiscalizador;
	}
	public List<Produto> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
    
    
	
}
