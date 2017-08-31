package server.room;

import java.util.HashMap;
import server.actors.Skill;
import server.actors.Spaceship;
import server.data.BattleData;
import server.data.LobbyData;
import server.room.battle.BattleRoomManager;
import server.room.lobby.LobbyUser;
import server.room.lobby.WaitingRoomManager;
import server.user.User;

public class Room {

    private static long globalId = 0;
    private final long matchTime;
    private final long id;
    private final int maxPlayersPerTeam;
    private final String name;
    private WaitingRoomManager waitingManager;
    private BattleRoomManager battleManager;
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
    
    public BattleRoomManager getBattleRoomManager(){
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
        this.battleManager = new BattleRoomManager(this, blueTeam, redTeam);
    }

    public long getMatchTime() {
        return this.matchTime;
    }

    public int getMaxPlayersPerTeam() {
        return this.maxPlayersPerTeam;
    }

    public void update() {
        if (this.battleManager != null) {
            this.battleManager.update();
        }
    }

    public void removeUser(User user) {
        this.waitingManager.removeUser(user.getId());
        if(this.battleManager != null){
            this.battleManager.removeUser(user.getId());
        }
        this.simpleRoom.decrementTotalPlayers();
    }
}
