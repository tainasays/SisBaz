package com.web.sisBaz.config;

import com.web.sisBaz.model.repositories.ConnectionManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Iniciando inicialização do banco de dados...");
        
        try {
            createSchemaIfNotExists();
         
            executeScript("schema.sql");
            System.out.println("Schema criado com sucesso!");
            
            //executeScript("data.sql");
            //System.out.println("Dados inseridos com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro durante a inicialização do banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createSchemaIfNotExists() throws Exception {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Criando schema 'sisbaz' se não existir...");
            stmt.execute("CREATE SCHEMA IF NOT EXISTS sisbaz");
            stmt.execute("SET search_path TO sisbaz, public");
            System.out.println("Schema 'sisbaz' ok");
        }
    }
    
    private void executeScript(String scriptName) throws Exception {
        ClassPathResource resource = new ClassPathResource(scriptName);
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String script = reader.lines()
                .filter(line -> !line.trim().startsWith("--") && !line.trim().isEmpty())
                .collect(Collectors.joining("\n"));
            
            try (Connection conn = ConnectionManager.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                String[] commands = script.split(";");
                
                for (String command : commands) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        System.out.println("Executando: " + command.substring(0, Math.min(50, command.length())) + "...");
                        try {
                            stmt.execute(command);
                            System.out.println("Comando executado com sucesso");
                        } catch (Exception e) {
                            System.err.println("Erro ao executar comando: " + e.getMessage());
                            throw e;
                        }
                    }
                }
            }
        }
    }
}
