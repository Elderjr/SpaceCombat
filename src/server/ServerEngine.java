package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.Room;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.data.User;
import server.exceptions.NotLoggedException;
import server.room.battle.PersonalStatistic;
import constants.Constants;
import server.dao.UserDAO;
import server.data.GeneralStatistics;

public class ServerEngine implements IServer {

    private static final ServerEngine instance = new ServerEngine();
    private static final long USER_TOLERANCE = 20000;
    private final HashMap<Long, LoggedUser> loggedUsers;
    private final HashMap<Long, Room> rooms;
    private final RoomData roomsData;

    private ServerEngine() {
        this.roomsData = new RoomData();
        this.loggedUsers = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    public static ServerEngine getInstance() {
        return instance;
    }

    public HashMap<Long, Room> getRooms() {
        return this.rooms;
    }

    @Override
    public void ping(long userId) throws RemoteException, NotLoggedException {
        LoggedUser loggedUser = this.loggedUsers.get(userId);
        if (loggedUser != null) {
            loggedUser.updateLastCommand();
        } else {
            throw new NotLoggedException();
        }
    }

    @Override
    public void exitGame(long userId) throws RemoteException {
        LoggedUser loggedUser = this.loggedUsers.get(userId);
        if (loggedUser != null) {
            disconnectUser(loggedUser.getUser());
            this.loggedUsers.remove(userId);
        }
    }

    public void addLoggedUser(User user) throws SQLException {
        GeneralStatistics userStatistic = UserDAO.getUserStatistics(user.getId());
        this.loggedUsers.put(user.getId(), new LoggedUser(user, userStatistic));
        this.roomsData.addUser(user);
    }

    @Override
    public User register(String username, String password) throws RemoteException {
        System.out.println("Register requested: " + username + " " + password);
        try {
            User user = UserDAO.registerUser(username, password);
            addLoggedUser(user);
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public User login(String username, String password) throws RemoteException {
        System.out.println("Login requested: " + username + " " + password);
        try {
            User user = UserDAO.login(username, password);
            if (user != null) {
                if (this.loggedUsers.containsKey(user.getId())) {
                    disconnectUser(user);
                    this.loggedUsers.remove(user.getId());
                }
                addLoggedUser(user);
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public SimpleRoom createRoom(long playerId, int playersPerTeam, long totalMatchTime, String name) throws RemoteException, NotLoggedException {
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
            loggedUser.updateLastCommand();
            return room.getSimpleRoom();
        } else {
            throw new NotLoggedException();
        }
    }

    @Override
    public SimpleRoom enterRoom(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room room = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (room != null && loggedUser != null) {
            System.out.println("User " + playerId + " entered in room " + roomId);
            boolean success = room.getWaitingRoomManager().addUser(loggedUser.getUser());
            loggedUser.updateLastCommand();
            if (success) {
                return room.getSimpleRoom();
            }
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
        return null;
    }

    @Override
    public void exitRoom(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            System.out.println("User " + playerId + " exited room " + roomId);
            r.removeUser(loggedUser.getUser());
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public void changeConfirm(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room room = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (room != null && loggedUser != null) {
            room.getWaitingRoomManager().changeConfirm(playerId);
            System.out.println("User " + playerId + " change confirm in room " + roomId);
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public void changeSpaceship(long playerId, long roomId, String actorType) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getWaitingRoomManager().changeSpaceship(playerId, actorType);
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public void changeTeam(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getWaitingRoomManager().changeTeam(playerId);
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public RoomData getRooms(long playerId) throws RemoteException, NotLoggedException {
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (loggedUser != null) {
            loggedUser.updateLastCommand();
            return this.roomsData;
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
        return null;
    }

    @Override
    public void move(long playerId, long roomId, int direction) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getBattleRoomManager().move(playerId, direction);
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public void useShot(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getBattleRoomManager().shoot(playerId);
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public void useSkill(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            r.getBattleRoomManager().useSkill(playerId);
            loggedUser.updateLastCommand();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
    }

    @Override
    public LobbyData getLobbyData(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            loggedUser.updateLastCommand();
            return r.getWaitingRoomManager().getLobbyData();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
        return null;
    }

    @Override
    public BattleData getBattleData(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room r = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (r != null && loggedUser != null) {
            loggedUser.updateLastCommand();
            return r.getBattleRoomManager().getBattleData(playerId);
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
        return null;
    }

    @Override
    public BattleStatistic getBattleStatistic(long playerId, long roomId) throws RemoteException, NotLoggedException {
        Room room = this.rooms.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (loggedUser != null && room != null && room.getState() == Constants.DONE) {
            loggedUser.updateLastCommand();
            return room.getBattleRoomManager().getBattleStatistic();
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
        return null;
    }

    @Override
    public GeneralStatistics getGeneralStatistic(long playerId) throws RemoteException, NotLoggedException {
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (loggedUser != null) {
            loggedUser.updateLastCommand();
            return loggedUser.getStatistics();
        } else {
            throw new NotLoggedException();
        }
    }

    public void updateStatistics(Room r) {
        BattleStatistic statistics = r.getBattleRoomManager().getBattleStatistic();
        boolean blueWins = statistics.getWinner() == Constants.BLUE_TEAM;
        boolean redWins = statistics.getWinner() == Constants.RED_TEAM;
        boolean draw = statistics.getWinner() == Constants.DRAW;
        try {
            for (PersonalStatistic statistic : statistics.getBlueTeam()) {
                LoggedUser loggedUser = this.loggedUsers.get(statistic.getUser().getId());
                if (loggedUser != null) {
                    loggedUser.incrementStatisticsValues(statistic, blueWins, draw);
                }
                UserDAO.updateStatistic(statistic, blueWins, draw);
            }
            for (PersonalStatistic statistic : statistics.getRedTeam()) {
                LoggedUser loggedUser = this.loggedUsers.get(statistic.getUser().getId());
                if (loggedUser != null) {
                    loggedUser.incrementStatisticsValues(statistic, redWins, draw);
                }
                UserDAO.updateStatistic(statistic, redWins, draw);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeRoomFromData(Room r) {
        this.roomsData.removeRoom(r.getSimpleRoom());
    }

    private void disconnectUser(User user) {
        this.roomsData.removeUser(user);
        for (Room r : this.rooms.values()) {
            r.getWaitingRoomManager().removeUser(user.getId());
            if (r.getBattleRoomManager() != null) {
                r.getBattleRoomManager().removeUser(user.getId());
            }
        }
    }

    public void update(long time) {
        Iterator<Room> roomIterator = this.rooms.values().iterator();
        Room room = null;
        while (roomIterator.hasNext()) {
            room = roomIterator.next();
            if (room.update(time)) {
                System.out.println(room.getId() + " deleted");
                removeRoomFromData(room);
                roomIterator.remove();
            }
        }
        Iterator<LoggedUser> iterator = this.loggedUsers.values().iterator();
        LoggedUser loggedUser = null;
        while (iterator.hasNext()) {
            loggedUser = iterator.next();
            if (loggedUser.isDisconnected(USER_TOLERANCE)) {
                System.out.println(loggedUser.getUser().getUsername() + " disconnected");;
                disconnectUser(loggedUser.getUser());
                iterator.remove();
            }
        }
    }

    public void mainLoop() {
        long lastUpdate = System.currentTimeMillis();
        while (true) {
            update(System.currentTimeMillis() - lastUpdate);
            lastUpdate = System.currentTimeMillis();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void start(String host) {
        try {
            System.out.println("Criando servidor (RMI)");
            System.setProperty("java.rmi.server.hostname", host);
            ServerEngine obj = ServerEngine.getInstance();
            IServer stub = (IServer) UnicastRemoteObject.exportObject(obj, 0);
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("SpaceCombat", stub);
            System.out.println("Checking database");
            try {
                UserDAO.createUserTable();
                System.out.println("Server Ready");
                obj.mainLoop();
            } catch (SQLException ex) {
                System.out.println("An error occured in database: " + ex.getMessage());
                System.exit(0);
            }
        } catch (RemoteException | AlreadyBoundException ex) {
            System.out.println("An error occured when creating a remote server: " + ex.getMessage());
            System.exit(0);
        }
    }

}
