package server.room;

import constants.Constants;
import server.data.User;
import java.util.HashMap;
import server.room.battle.BattleManager;
import server.data.LobbyUser;
import server.room.lobby.WaitingRoomManager;

public class Room {

    private static long globalId = 0;
    private final long matchTime;
    private final long id;
    private final int maxPlayersPerTeam;
    private final String name;
    private int state;
    private long doneAt;
    private WaitingRoomManager waitingManager;
    private BattleManager battleManager;
    private SimpleRoom simpleRoom;

    public Room(int maxPlayersPerTeam, long matchTime, String name) {
        this.name = name;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
        this.matchTime = matchTime;
        this.battleManager = null;
        this.id = globalId;
        this.simpleRoom = new SimpleRoom(id, maxPlayersPerTeam, matchTime, name);
        this.waitingManager = new WaitingRoomManager(this);
        this.setState(Constants.WAITING);
        globalId++;
    }

    
    public SimpleRoom getSimpleRoom() {
        return this.simpleRoom;
    }

    public WaitingRoomManager getWaitingRoomManager(){
        return this.waitingManager;
    }
    
    public BattleManager getBattleRoomManager(){
        return this.battleManager;
    }
    
    public long getId() {
        return this.id;
    }

    public int getState(){
        return this.state;
    }
    
    public void setState(int state) {
        this.state = state;
        this.simpleRoom.setState(state);
    }
    
    public String getName() {
        return this.name;
    }

    public long getMatchTime() {
        return this.matchTime;
    }

    public int getMaxPlayersPerTeam() {
        return this.maxPlayersPerTeam;
    }

    public void removeUser(User user) {
        if(this.state == Constants.WAITING){
            this.waitingManager.removeUser(user.getId());
            this.simpleRoom.decrementTotalPlayers();
        }else if(this.state == Constants.PLAYING){
            this.battleManager.removeUser(user.getId());
            this.simpleRoom.decrementTotalPlayers();
        }
    }

    public void startBattle(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        this.battleManager = new BattleManager(blueTeam, redTeam, this);
        this.setState(Constants.PLAYING);
    }
    
    public void endBattle(){
        this.setState(Constants.DONE);
        this.doneAt = System.currentTimeMillis();
        server.ServerEngine.getInstance().updateStatistics(this);
        server.ServerEngine.getInstance().removeRoomFromData(this);
        System.out.println("done");
    }
    
    public boolean update(long time) {
        if (this.state == Constants.PLAYING) {
            this.battleManager.update(time);
        }
        return (this.state == Constants.DONE && System.currentTimeMillis() - this.doneAt >= 25000)
                || (this.state == Constants.WAITING && this.getWaitingRoomManager().getTotalPlayers() == 0);
    }
}
