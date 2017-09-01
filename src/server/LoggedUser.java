package server;

import server.data.User;

public class LoggedUser {

    private User user;
    private long lastCommand;

    public LoggedUser(User user) {
        this.user = user;
        updateLastCommand();
    }

    public User getUser() {
        return this.user;
    }

    public void updateLastCommand(){
        this.lastCommand = System.currentTimeMillis();
    }
    
    public boolean isDisconnected(long limit) {
        return System.currentTimeMillis() - this.lastCommand >= limit;
    }
}
