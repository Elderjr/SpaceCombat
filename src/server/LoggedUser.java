package server;

import server.user.User;

public class LoggedUser {

	private User user;
	private long lastCommand;
	
	public LoggedUser(User user){
		this.user = user;
		this.lastCommand = System.currentTimeMillis();
	}
	
	public User getUser(){
		return this.user;
	}
	
	public boolean isDisconnected(long limit){
		return System.currentTimeMillis() - this.lastCommand >= limit;
	}
}
