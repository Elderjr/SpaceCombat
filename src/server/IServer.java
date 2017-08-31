package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.user.User;

public interface IServer extends Remote{

    public void ping() throws RemoteException;
    
    public boolean register(String username, String password) throws RemoteException;

    public User login(String username, String password) throws RemoteException;

    public SimpleRoom createRoom(long playerId, int maxPlayers, long totalMatchTime, String name) throws RemoteException;

    public SimpleRoom enterRoom(long playerId, long roomId) throws RemoteException;

    public void changeConfirm(long playerId, long roomId) throws RemoteException;

    public void changeTeam(long playerId, long roomId) throws RemoteException;
    
    public void changeSpaceship(long playerId, long roomId, String actorType) throws RemoteException;
    
    public void exitRoom(long playerId, long roomId) throws RemoteException;

    public RoomData getRooms(long playerId) throws RemoteException;

    public void move(long playerId, long roomId, int direction) throws RemoteException;

    public void useShot(long playerId, long roomId) throws RemoteException;

    public void useSkill(long playerId, long roomId) throws RemoteException;

    public LobbyData getLobbyData(long playerId, long roomId) throws RemoteException;
    
    public BattleData getBattleData(long playerId, long roomId) throws RemoteException;
    
    public BattleStatistic getBattleStatistic(long playerId, long roomId) throws RemoteException;
}
