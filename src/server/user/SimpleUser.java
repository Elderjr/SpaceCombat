package server.user;

import java.io.Serializable;

public class SimpleUser implements Serializable{

    private long id;
    private String username;

    public SimpleUser(long id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public long getId(){
        return this.id;
    }
    
    public String getUsername(){
        return this.username;
    }
}
