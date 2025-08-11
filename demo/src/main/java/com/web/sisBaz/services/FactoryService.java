package com.web.sisBaz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactoryService {
    
    @Autowired
    private LoteService loteService;
    
    @Autowired
    private OrgaoDonatarioService orgaoDonatarioService;
    
    @Autowired
    private OrgaoFiscalizadorService orgaoFiscalizadorService;
    
    @Autowired
    private ProdutoService produtoService;
    
    public LoteService getLoteService() {
        return loteService;
    }
    
    public OrgaoDonatarioService getOrgaoDonatarioService() {
        return orgaoDonatarioService;
    }
    
    public OrgaoFiscalizadorService getOrgaoFiscalizadorService() {
        return orgaoFiscalizadorService;
    }
    
    public ProdutoService getProdutoService() {
        return produtoService;
    }
}