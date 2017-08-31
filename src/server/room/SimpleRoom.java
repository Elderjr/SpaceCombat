package server.room;

import java.io.Serializable;

public class SimpleRoom implements Serializable{

    private final long id;
    private final int maxPlayersPerTeam;
    private final long matchTime;
    private final String name;
    private int totalPlayers;
    private int state;
    
    public SimpleRoom(long id, int maxPlayersPerTeam, long matchTime, String name) {
        this.id = id;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
        this.matchTime = matchTime;
        this.totalPlayers = 0;
        this.state = 0;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public void incrementTotalPlayers() {
        this.totalPlayers++;
    }

    public void decrementTotalPlayers() {
        this.totalPlayers--;
    }

    public String getName() {
        return this.name;
    }
    
    public long getMathTime(){
        return this.matchTime;
    }
    
    public int getMaxPlayersPerTeam(){
        return this.maxPlayersPerTeam;
    }
    
    public int getTotalPlayers(){
        return this.totalPlayers;
    }
}
