package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.data.User;
import server.exceptions.NotLoggedException;

public interface IServer extends Remote{

    public void ping() throws RemoteException;
    
    public User register(String username, String password) throws RemoteException;

    public User login(String username, String password) throws RemoteException;

    public SimpleRoom createRoom(long playerId, int maxPlayers, long totalMatchTime, String name) throws RemoteException, NotLoggedException;

    public SimpleRoom enterRoom(long playerId, long roomId) throws RemoteException, NotLoggedException;

    public void changeConfirm(long playerId, long roomId) throws RemoteException, NotLoggedException;

    public void changeTeam(long playerId, long roomId) throws RemoteException, NotLoggedException;
    
    public void changeSpaceship(long playerId, long roomId, String actorType) throws RemoteException, NotLoggedException;
    
    public void exitRoom(long playerId, long roomId) throws RemoteException, NotLoggedException;

    public RoomData getRooms(long playerId) throws RemoteException, NotLoggedException;

    public void move(long playerId, long roomId, int direction) throws RemoteException, NotLoggedException;

    public void useShot(long playerId, long roomId) throws RemoteException, NotLoggedException;

    public void useSkill(long playerId, long roomId) throws RemoteException, NotLoggedException;

    public LobbyData getLobbyData(long playerId, long roomId) throws RemoteException, NotLoggedException;
    
    public BattleData getBattleData(long playerId, long roomId) throws RemoteException, NotLoggedException;
    
    public BattleStatistic getBattleStatistic(long playerId, long roomId) throws RemoteException, NotLoggedException;
}
