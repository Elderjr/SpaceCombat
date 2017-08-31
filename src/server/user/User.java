package server.user;

import java.io.Serializable;

public class User implements Serializable{

	private long id;
	private String username;
	
	
	public User(long id, String username){
		this.id = id;
		this.username = username;
	}
	
	public long getId(){
		return this.id;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public SimpleUser getSimpleUser(){
		return new SimpleUser(id, username);
	}
	
	@Override
	public String toString(){
		return id+"-"+username;
	}
}
