package server;

import java.util.HashMap;

import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.Room;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.user.User;
import server.user.UserDAO;

public class ServerEngine implements IServer {

    private static ServerEngine instance = new ServerEngine();

    private HashMap<Long, LoggedUser> loggedUsers;
    private HashMap<Long, Room> rooms;
    private RoomData roomsData;

    private ServerEngine() {
        this.roomsData = new RoomData();
        this.loggedUsers = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    public static ServerEngine getInstance() {
        return instance;
    }

    @Override
    public boolean register(String username, String password) {
        return false;
    }

    @Override
    public User login(String username, String password) {
        System.out.println("Login requested: " + username + " " + password);
        User user = UserDAO.login(username, password);
        if (user != null) {
            System.out.println("Login success");
            loggedUsers.put(user.getId(), new LoggedUser(user));
        } else {
            System.out.println("Login fail");
        }
        return user;
    }

    @Override
    public SimpleRoom createRoom(long playerId, int playersPerTeam, long totalMatchTime, String name) {
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        long roomId = -1;
        if (loggedUser != null) {
            Room room = new Room(playersPerTeam, totalMatchTime, name);
            System.out.println("User " + playerId + " creates room  " + room.getId() + " (players per team: " + playersPerTeam + "; time: "
                    + totalMatchTime + ")");
            roomId = room.getId();
            room.getWaitingRoomManager().addUser(loggedUser.getUser());
            rooms.put(room.getId(), room);
            roomsData.addRoom(room.getSimpleRoom());
            return room.getSimpleRoom();
        } else {
            System.out.println("User " + playerId + " not logged");
            return null;
        }
    }

    @Override
    public SimpleRoom enterRoom(long playerId, long roomId) {
        Room room = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (room != null && loggedUser != null) {
            System.out.println("User " + playerId + " entered in room " + roomId);
            boolean success = room.getWaitingRoomManager().addUser(loggedUser.getUser());
            if (success) {
                return room.getSimpleRoom();
            }
        } else {
            System.out.println("Fail to enter in room (Room or logged user not found)");
        }
        return null;
    }

    @Override
    public void exitRoom(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            System.out.println("User " + playerId + " exited room " + roomId);
            r.removeUser(loggedUser.getUser());
        }
    }

    @Override
    public void changeConfirm(long playerId, long roomId) {
        Room room = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (room != null && loggedUser != null) {
            room.getWaitingRoomManager().changeConfirm(playerId);
            System.out.println("User " + playerId + " change confirm in room " + roomId);
        } else {
            System.out.println("Fail to change confirm in room (Room or logged user not found)");
        }
    }

    @Override
    public void changeSpaceship(long playerId, long roomId, String actorType) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getWaitingRoomManager().changeSpaceship(playerId, actorType);
        }
    }

    @Override
    public void changeTeam(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getWaitingRoomManager().changeTeam(playerId);
        }
    }

    @Override
    public RoomData getRooms(long playerId) {
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (loggedUser != null) {
            return this.roomsData;
        }
        return null;
    }

    @Override
    public void move(long playerId, long roomId, int direction) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getBattleRoomManager().move(playerId, direction);
        }
    }

    @Override
    public void useShot(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getBattleRoomManager().useShot(playerId);
        }
    }

    @Override
    public void useSkill(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getBattleRoomManager().useSkill(playerId);
        }
    }

    @Override
    public LobbyData getLobbyData(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            return r.getWaitingRoomManager().getLobbyData();
        }
        return null;
    }
    
    @Override
    public BattleData getBattleData(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            return r.getBattleRoomManager().getBattleData(playerId);
        }
        return null;
    }

    @Override
    public BattleStatistic getBattleStatistic(long playerId, long roomId) {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            return r.getBattleRoomManager().getBattleStatistic();
        }
        return null;
    }

    public void update() {
        for (Room r : this.rooms.values()) {
            r.update();
        }
    }

    public void mainLoop() {
        while (true) {
            try {
                Thread.sleep(30);
                update();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
        }
    }
}
