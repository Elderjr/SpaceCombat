package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.Room;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.data.User;
import server.exceptions.NotLoggedException;
import server.room.battle.PersonalStatistic;
import server.serverConstants.ServerConstants;
import server.user.UserDAO;

public class ServerEngine implements IServer {

    private static ServerEngine instance = new ServerEngine();

    private HashMap<Long, LoggedUser> loggedUsers;
    private HashMap<Long, Room> rooms;
    private HashMap<Long, BattleStatistic> roomsStatistics;
    private RoomData roomsData;

    private ServerEngine() {
        this.roomsData = new RoomData();
        this.loggedUsers = new HashMap<>();
        this.rooms = new HashMap<>();
        this.roomsStatistics = new HashMap<>();
    }

    public static ServerEngine getInstance() {
        return instance;
    }

    @Override
    public void ping() {

    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        return false;
    }

    @Override
    public User login(String username, String password) throws RemoteException {
        System.out.println("Login requested: " + username + " " + password);
        User user = UserDAO.login(username, password);
        if (user != null && !loggedUsers.containsKey(user.getId())) {
            System.out.println("Login success");
            this.loggedUsers.put(user.getId(), new LoggedUser(user));
            this.roomsData.addUser(user);
        } else {
            System.out.println("Login fail");
        }
        return user;
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
        BattleStatistic statistics = this.roomsStatistics.get(roomId);
        LoggedUser loggedUser = this.loggedUsers.get(playerId);
        if (statistics != null && loggedUser != null) {
            loggedUser.updateLastCommand();
            return statistics;
        } else if (loggedUser == null) {
            throw new NotLoggedException();
        }
        return null;
    }

    private void endRoom(long roomId) {
        Room r = this.rooms.get(roomId);
        if (r != null) {
            BattleStatistic statistics = r.getBattleRoomManager().getBattleStatistic();
            this.roomsStatistics.put(r.getId(), statistics);
            boolean blueWins = statistics.getWinner() == ServerConstants.BLUE_TEAM;
            boolean redWins = statistics.getWinner() == ServerConstants.RED_TEAM;
            boolean draw = statistics.getWinner() == ServerConstants.DRAW;
            for (PersonalStatistic statistic : statistics.getBlueTeam()) {
                UserDAO.updateStatistic(statistic, blueWins, draw);
            }
            for (PersonalStatistic statistic : statistics.getRedTeam()) {
                UserDAO.updateStatistic(statistic, redWins, draw);
            }
            removeRoom(r.getId());
        }
    }

    public void removeRoom(long roomId) {
        Room r = this.rooms.get(roomId);
        this.roomsData.removeRoom(r.getSimpleRoom());
        this.rooms.remove(roomId);
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

    public void update() {
        for (Room r : this.rooms.values()) {
            if (r.update()) {
                endRoom(r.getId());
            }
        }
        Iterator<LoggedUser> iterator = this.loggedUsers.values().iterator();
        LoggedUser loggedUser = null;
        while (iterator.hasNext()) {
            loggedUser = iterator.next();
            if (loggedUser.isDisconnected(10000)) {
                System.out.println(loggedUser.getUser().getUsername() + " disconnected");;
                disconnectUser(loggedUser.getUser());
                iterator.remove();
            }
        }
    }

    public void mainLoop() {
        while (true) {
            update();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void start(String host) {
        try {
            //String host = JOptionPane.showInputDialog("Ip server: ");
            System.out.println("Criando servidor (RMI)");
            System.setProperty("java.rmi.server.hostname", host);
            ServerEngine obj = ServerEngine.getInstance();
            IServer stub = (IServer) UnicastRemoteObject.exportObject(obj, 0);
            LocateRegistry.createRegistry(1099);
            //Register the remote object with a Java RMI registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("SpaceCombat", stub);
            System.out.println("Server Ready");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    obj.mainLoop();
                }
            }).start();
        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(ServerEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
