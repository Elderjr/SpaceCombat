/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.IServer;
import server.ServerEngine;
import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.data.User;
import server.exceptions.NotLoggedException;

/**
 *
 * @author elderjr
 */
public class ClientNetwork {

    private static final ClientNetwork instance = new ClientNetwork();
    private IServer server;
    private User user;

    public static ClientNetwork getInstance() {
        return instance;
    }

    private ClientNetwork() {
        try {
            this.server = ServerEngine.getInstance();
            User user = this.login("sara", "123");
            SimpleRoom room = this.createRoom(1, 10000, "meu amor");
            this.server.changeConfirm(this.user.getId(), room.getId());
        } catch (RemoteException | NotLoggedException ex) {
            Logger.getLogger(ClientNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean connect(String host) {
        /*
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            this.server = (IServer) registry.lookup("SpaceCombat");
            return true;
        } catch (RemoteException | NotBoundException ex) {
            return false;
        } 
         */
        return true;
    }

    public User register(String username, String password) throws RemoteException {
        this.user = server.register(username, password);
        return this.user;
    }

    public User login(String username, String password) throws RemoteException {
        this.user = server.login(username, password);
        return this.user;
    }

    public RoomData getRooms() throws RemoteException, NotLoggedException {
        return this.server.getRooms(this.user.getId());
    }

    public SimpleRoom createRoom(int maxPlayers, long matchTime, String name) throws RemoteException, NotLoggedException {
        return this.server.createRoom(this.user.getId(), maxPlayers, matchTime, name);
    }

    public SimpleRoom enterRoom(long roomId) throws RemoteException, NotLoggedException {
        return this.server.enterRoom(this.user.getId(), roomId);
    }

    public void exitRoom(long roomId) throws RemoteException, NotLoggedException {
        this.server.exitRoom(this.user.getId(), roomId);
    }

    public void changeConfirm(long roomId) throws RemoteException, NotLoggedException {
        this.server.changeConfirm(this.user.getId(), roomId);
    }

    public void changeTeam(long roomId) throws RemoteException, NotLoggedException {
        this.server.changeTeam(this.user.getId(), roomId);
    }

    public void changeSpacechip(long roomId, String actorType) throws RemoteException, NotLoggedException {
        this.server.changeSpaceship(this.user.getId(), roomId, actorType);
    }

    public LobbyData getLobbyData(long roomId) throws RemoteException, NotLoggedException {
        return this.server.getLobbyData(this.user.getId(), roomId);
    }

    public BattleData getBattleData(long roomId) throws RemoteException, NotLoggedException {
        return this.server.getBattleData(this.user.getId(), roomId);
    }

    public BattleStatistic getBattleStatistic(long roomId) throws RemoteException, NotLoggedException {
        return this.server.getBattleStatistic(this.user.getId(), roomId);
    }

    public void useShot(long roomId) throws RemoteException, NotLoggedException {
        this.server.useShot(this.user.getId(), roomId);
    }

    public void useSkill(long roomId) throws RemoteException, NotLoggedException {
        this.server.useSkill(this.user.getId(), roomId);
    }

    public void move(long roomId, int direction) throws RemoteException, NotLoggedException {
        this.server.move(this.user.getId(), roomId, direction);
    }
}
