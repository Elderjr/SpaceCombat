package server.room.lobby;

import java.util.HashMap;

import server.actors.ActorsTypes;
import server.data.LobbyData;
import server.room.Room;
import server.user.User;

public class WaitingRoomManager {

    private Room room;
    private HashMap<Long, LobbyUser> blueTeam;
    private HashMap<Long, LobbyUser> redTeam;
    private LobbyData lobbyData;

    public WaitingRoomManager(Room room) {
        this.room = room;
        this.blueTeam = new HashMap<>();
        this.redTeam = new HashMap<>();
        this.lobbyData = new LobbyData(blueTeam, redTeam);
        room.setState(0);
    }

    public int getTotalPlayers() {
        return this.blueTeam.size() + this.redTeam.size();
    }

    public boolean addUser(User user) {
        if (blueTeam.size() < room.getMaxPlayersPerTeam() || redTeam.size() < room.getMaxPlayersPerTeam()) {
            if (blueTeam.size() >= redTeam.size()) {
                System.out.println("User " + user.getId() + " entered in red team (room " + room.getId() + ")");
                redTeam.put(user.getId(), new LobbyUser(user.getSimpleUser(), ActorsTypes.SPACESHIP_ASSAULTER, false));
            } else {
                blueTeam.put(user.getId(), new LobbyUser(user.getSimpleUser(), ActorsTypes.SPACESHIP_ASSAULTER, false));
                System.out.println("User " + user.getId() + " entered in blue team (room " + room.getId() + ")");
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
            startBattle();
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

    public void startBattle() {
        room.startBattle(blueTeam, redTeam);
    }
}
