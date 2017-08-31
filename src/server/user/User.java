package server.user;

import java.io.Serializable;

public class User implements Serializable{

    private long id;
    private String username;

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public long getId(){
        return this.id;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    @Override
    public int hashCode(){
        return this.username.length();
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof User){
            return ((User)obj).id == this.id;
        }
        return false;
    }
}
