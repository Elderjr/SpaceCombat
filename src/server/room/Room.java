package server.room;

import server.data.User;
import java.util.HashMap;
import server.actors.Skill;
import server.actors.Spaceship;
import server.data.BattleData;
import server.data.LobbyData;
import server.room.battle.BattleManager;
import server.data.LobbyUser;
import server.room.lobby.WaitingRoomManager;

public class Room {

    private static long globalId = 0;
    private final long matchTime;
    private final long id;
    private final int maxPlayersPerTeam;
    private final String name;
    private WaitingRoomManager waitingManager;
    private BattleManager battleManager;
    private int state;
    private SimpleRoom simpleRoom;

    public Room(int maxPlayersPerTeam, long matchTime, String name) {
        this.name = name;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
        this.matchTime = matchTime;
        this.battleManager = null;
        this.id = globalId;
        this.simpleRoom = new SimpleRoom(id, maxPlayersPerTeam, matchTime, name);
        this.waitingManager = new WaitingRoomManager(this);
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

    public void setState(int state) {
        this.state = state;
        this.simpleRoom.setState(state);
    }

    public void startBattle(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        this.battleManager = new BattleManager(blueTeam, redTeam, this.matchTime);
    }

    public long getMatchTime() {
        return this.matchTime;
    }

    public int getMaxPlayersPerTeam() {
        return this.maxPlayersPerTeam;
    }

    public boolean update() {
        if (this.battleManager != null) {
            return this.battleManager.update();
        }
        return false;
    }

    public void removeUser(User user) {
        this.waitingManager.removeUser(user.getId());
        if(this.battleManager != null){
            this.battleManager.removeUser(user.getId());
        }
        this.simpleRoom.decrementTotalPlayers();
    }
}
