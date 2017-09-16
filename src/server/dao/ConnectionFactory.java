package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static final ConnectionFactory instance = new ConnectionFactory();
    
    private String user;
    private String password;
    
    private ConnectionFactory(){
        this.user = "root";
        this.password = "";
    }
    
    public static ConnectionFactory getInstance(){
        return instance;
    }
    
    public void setUser(String user){
        this.user = user;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/spacecombat", this.user, this.password);
    }
}
