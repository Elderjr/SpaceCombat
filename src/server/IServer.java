package server;

import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.user.User;

public interface IServer {

    public boolean register(String username, String password);

    public User login(String username, String password);

    public SimpleRoom createRoom(long playerId, int maxPlayers, long totalMatchTime, String name);

    public SimpleRoom enterRoom(long playerId, long roomId);

    public void changeConfirm(long playerId, long roomId);

    public void changeTeam(long playerId, long roomId);
    
    public void changeSpaceship(long playerId, long roomId, String actorType);
    
    public void exitRoom(long playerId, long roomId);

    public RoomData getRooms(long playerId);

    public void move(long playerId, long roomId, int direction);

    public void useShot(long playerId, long roomId);

    public void useSkill(long playerId, long roomId);

    public LobbyData getLobbyData(long playerId, long roomId);
    
    public BattleData getBattleData(long playerId, long roomId);
    
    public BattleStatistic getBattleStatistic(long playerId, long roomId);
}
