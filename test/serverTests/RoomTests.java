/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTests;

import constants.Constants;
import org.junit.Test;
import static org.junit.Assert.*;
import server.data.User;
import server.room.Room;

/**
 *
 * @author elderjr
 */
public class RoomTests {
    
    public RoomTests() {
    }
    
    @Test
    public void waitingTest(){
        Room room = new Room(2, 1000, "room test");
        assertEquals(Constants.WAITING, room.getState());
        assertEquals(room.getId(), room.getSimpleRoom().getId());
        assertEquals(room.getMatchTime(), room.getSimpleRoom().getMathTime());
        assertEquals(room.getMaxPlayersPerTeam(), room.getSimpleRoom().getMaxPlayersPerTeam());
        assertEquals(room.getName(), room.getSimpleRoom().getName());
        assertEquals(room.getState(), room.getSimpleRoom().getState());        
        
    }
    
    @Test
    public void changeConfirmTest(){
        Room room = new Room(2, 1000, "room test");
        User userA = new User(1, "A");
        room.getWaitingRoomManager().addUser(userA);
        room.getWaitingRoomManager().changeConfirm(userA.getId());
        assertEquals(true, room.getWaitingRoomManager().getLobbyUser(userA.getId()).isConfirmed());
    }
    
    
    @Test
    public void changeTeamTest(){
        Room room = new Room(2, 1000, "room test");
        User userA = new User(1, "A");
        room.getWaitingRoomManager().addUser(userA);
        assertEquals(room.getWaitingRoomManager().getBlueTeamLobbyUser(userA.getId()) != null, true);
        assertEquals(room.getWaitingRoomManager().getRedTeamLobbyUser(userA.getId()) != null, false);
        room.getWaitingRoomManager().changeTeam(userA.getId());
        assertEquals(false, room.getWaitingRoomManager().getBlueTeamLobbyUser(userA.getId()) != null);
        assertEquals(true, room.getWaitingRoomManager().getRedTeamLobbyUser(userA.getId()) != null);
    }
    
    @Test
    public void removeUserTest(){
        Room room = new Room(2, 1000, "room test");
        User userA = new User(1, "A");
        room.getWaitingRoomManager().addUser(userA);
        assertEquals(true, room.getWaitingRoomManager().getBlueTeamLobbyUser(userA.getId()) != null);
        room.getWaitingRoomManager().removeUser(userA.getId());
        assertEquals(false, room.getWaitingRoomManager().getBlueTeamLobbyUser(userA.getId()) != null);
    }
    
    @Test
    public void batteTest(){
        Room room = new Room(1, 1000, "room test");
        User userA = new User(1, "A");
        User userB = new User(2, "B");
        room.getWaitingRoomManager().addUser(userA);
        room.getWaitingRoomManager().addUser(userB);
        assertEquals(Constants.WAITING, room.getState());
        assertEquals(Constants.WAITING, room.getSimpleRoom().getState());
        room.getWaitingRoomManager().changeConfirm(userA.getId());
        room.getWaitingRoomManager().changeConfirm(userB.getId());
        assertEquals(Constants.PLAYING, room.getState());
        assertEquals(Constants.PLAYING, room.getSimpleRoom().getState());
    }
}
