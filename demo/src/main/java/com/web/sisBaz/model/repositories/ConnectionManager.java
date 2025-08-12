package com.web.sisBaz.model.repositories;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionManager {
    
    private static final String URL = "jdbc:postgresql://localhost:5435/sisbaz";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";
    
    private static Connection currentConnection = null;
    
    public static Connection getCurrentConnection() throws SQLException, ClassNotFoundException{
        Class.forName("com.postgresql.cj.jdbc.Driver");
        
        if(currentConnection == null){
            currentConnection = DriverManager.getConnection(URL, USER, PASSWORD);
      
            try (Statement stmt = currentConnection.createStatement()) {
                stmt.execute("SET search_path TO sisbaz, public");
            }
        }
        return currentConnection;
    }
    
    public static Connection getConnection() throws SQLException{
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
       
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET search_path TO sisbaz, public");
        } catch (SQLException e) {
        	 
        	if (conn != null && !conn.isClosed()) {
                conn.close();  
            }
            throw e; 
        }
        return conn;
    }
    
}

