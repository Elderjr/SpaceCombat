package server.room.lobby;

import constants.Constants;
import server.data.LobbyUser;
import java.util.HashMap;
import java.util.LinkedHashMap;
import server.ServerEngine;
import server.data.LobbyData;
import server.room.Room;
import server.data.User;

public class WaitingRoomManager {

    private final Room room;
    private final HashMap<Long, LobbyUser> blueTeam;
    private final HashMap<Long, LobbyUser> redTeam;
    private final LobbyData lobbyData;

    public WaitingRoomManager(Room room) {
        this.room = room;
        this.blueTeam = new LinkedHashMap<>();
        this.redTeam = new LinkedHashMap<>();
        this.lobbyData = new LobbyData(blueTeam, redTeam);
    }

    
    public int getTotalPlayers() {
        return this.blueTeam.size() + this.redTeam.size();
    }

    public boolean addUser(User user) {
        if (room.getState() == Constants.WAITING && (this.redTeam.size() < this.room.getMaxPlayersPerTeam() || this.blueTeam.size() < this.room.getMaxPlayersPerTeam())) {
            if (this.redTeam.size() >= this.blueTeam.size()) {
                System.out.println("User " + user.getId() + " entered in blue team (room " + room.getId() + ")");
                blueTeam.put(user.getId(), new LobbyUser(user, Constants.SPACESHIP_ASSAULTER, false));
            } else {
                redTeam.put(user.getId(), new LobbyUser(user, Constants.SPACESHIP_ASSAULTER, false));
                System.out.println("User " + user.getId() + " entered in red team (room " + room.getId() + ")");
            }
            room.getSimpleRoom().incrementTotalPlayers();
            return true;
        }
        return false;
    }

    public void changeTeam(long userId) {
        if (blueTeam.containsKey(userId) /* && redTeam.size() < room.getMaxPlayersPerTeam() */) {
            LobbyUser lobbyUser = blueTeam.get(userId);
            blueTeam.remove(userId);
            redTeam.put(userId, lobbyUser);
        } else if (redTeam.containsKey(userId) /* && blueTeam.size() < room.getMaxPlayersPerTeam() */) {
            LobbyUser lobbyUser = redTeam.get(userId);
            redTeam.remove(userId);
            blueTeam.put(userId, lobbyUser);
        }
    }

    public void changeConfirm(long userId) {
        if (blueTeam.containsKey(userId)) {
            blueTeam.get(userId).changeConfirm();
        } else if (redTeam.containsKey(userId)) {
            redTeam.get(userId).changeConfirm();
        }
        if (isReady()) {
            System.out.println("ready!");
            room.startBattle(blueTeam, redTeam);
        }else{
            System.out.println("not ready");
        }
    }

    public void changeSpaceship(long userId, String actorType) {
        if (blueTeam.containsKey(userId)) {
            blueTeam.get(userId).setSpaceshipSelected(actorType);
        } else if (redTeam.containsKey(userId)) {
            redTeam.get(userId).setSpaceshipSelected(actorType);
        }
    }

    public void removeUser(long userId) {
        blueTeam.remove(userId);
        redTeam.remove(userId);
    }

    public LobbyUser getBlueTeamLobbyUser(long userId){
        return this.blueTeam.get(userId);
    }
    
    public LobbyUser getRedTeamLobbyUser(long userId){
        return this.redTeam.get(userId);
    }
    public LobbyUser getLobbyUser(long userId){
        if(blueTeam.containsKey(userId)){
            return blueTeam.get(userId);
        }
        return redTeam.get(userId);
    }
    private boolean isReady() {
        if (blueTeam.size() == room.getMaxPlayersPerTeam()
                && redTeam.size() == room.getMaxPlayersPerTeam()) {
            for (LobbyUser lobbyUser : blueTeam.values()) {
                if (!lobbyUser.isConfirmed()) {
                    return false;
                }
            }
            for (LobbyUser lobbyUser : redTeam.values()) {
                if (!lobbyUser.isConfirmed()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public LobbyData getLobbyData() {
        return this.lobbyData;
    }

}
