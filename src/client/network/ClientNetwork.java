/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.IServer;
import server.ServerEngine;
import server.data.BattleData;
import server.data.LobbyData;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;
import server.user.User;

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
        this.server = ServerEngine.getInstance();
        this.user = null;
    }

    public boolean connect(String host) {
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            this.server = (IServer) registry.lookup("SpaceCombat");
            return true;
        } catch (RemoteException | NotBoundException ex) {
            return false;
        } 
    }

    public boolean login(String username, String password) throws RemoteException {
        this.user = server.login(username, password);
        return this.user != null;
    }

    public RoomData getRooms() throws RemoteException {
        return this.server.getRooms(this.user.getId());
    }

    public SimpleRoom createRoom(int maxPlayers, long matchTime, String name) throws RemoteException {
        return this.server.createRoom(this.user.getId(), maxPlayers, matchTime, name);
    }

    public SimpleRoom enterRoom(long roomId) throws RemoteException {
        return this.server.enterRoom(this.user.getId(), roomId);
    }

    public void exitRoom(long roomId) throws RemoteException {
        this.server.exitRoom(this.user.getId(), roomId);
    }

    public void changeConfirm(long roomId) throws RemoteException {
        this.server.changeConfirm(this.user.getId(), roomId);
    }

    public void changeTeam(long roomId) throws RemoteException {
        this.server.changeTeam(this.user.getId(), roomId);
    }

    public void changeSpacechip(long roomId, String actorType) throws RemoteException {
        this.server.changeSpaceship(this.user.getId(), roomId, actorType);
    }

    public LobbyData getLobbyData(long roomId) throws RemoteException {
        return this.server.getLobbyData(this.user.getId(), roomId);
    }

    public BattleData getBattleData(long roomId) throws RemoteException {
        return this.server.getBattleData(this.user.getId(), roomId);
    }

    public BattleStatistic getBattleStatistic(long roomId) throws RemoteException {
        return this.server.getBattleStatistic(this.user.getId(), roomId);
    }

    public void useShot(long roomId) throws RemoteException {
        this.server.useShot(this.user.getId(), roomId);
    }

    public void useSkill(long roomId) throws RemoteException {
        this.server.useSkill(this.user.getId(), roomId);
    }

    public void move(long roomId, int direction) throws RemoteException {
        this.server.move(this.user.getId(), roomId, direction);
    }
}
