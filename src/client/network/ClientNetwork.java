/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

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
        User sara = this.server.login("sara", "123");
        SimpleRoom room = this.server.createRoom(sara.getId(), 1, 3000, "Sara ama Elder");
        this.server.changeConfirm(sara.getId(), room.getId());
        this.user = null;
    }

    public boolean login(String username, String password) {
        System.out.println("hi");
        this.user = server.login(username, password);
        return this.user != null;
    }

    public RoomData getRooms() {
        return this.server.getRooms(this.user.getId());
    }

    public SimpleRoom createRoom(int maxPlayers, long matchTime, String name) {
        return this.server.createRoom(this.user.getId(), maxPlayers, matchTime, name);
    }

    public SimpleRoom enterRoom(long roomId) {
        return this.server.enterRoom(this.user.getId(), roomId);
    }

    public void exitRoom(long roomId) {
        this.server.exitRoom(this.user.getId(), roomId);
    }

    public void changeConfirm(long roomId) {
        this.server.changeConfirm(this.user.getId(), roomId);
    }

    public void changeTeam(long roomId) {
        this.server.changeTeam(this.user.getId(), roomId);
    }

    public void changeSpacechip(long roomId, String actorType) {
        this.server.changeSpaceship(this.user.getId(), roomId, actorType);
    }

    public LobbyData getLobbyData(long roomId) {
        return this.server.getLobbyData(this.user.getId(), roomId);
    }

    public BattleData getBattleData(long roomId) {
        return this.server.getBattleData(this.user.getId(), roomId);
    }
    
    public BattleStatistic getBattleStatistic(long roomId){
        return this.server.getBattleStatistic(this.user.getId(), roomId);
    }

    public void useShot(long roomId){
        this.server.useShot(this.user.getId(), roomId);
    }
    
    public void useSkill(long roomId){
        this.server.useSkill(this.user.getId(), roomId);
    }
    
    public void move(long roomId, int direction){
        this.server.move(this.user.getId(), roomId, direction);
    }
    
    public boolean connect(String ip) {
        return true;
    }
}
