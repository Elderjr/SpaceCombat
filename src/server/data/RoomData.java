package server.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import server.room.SimpleRoom;

public class RoomData implements Serializable {

    private List<SimpleRoom> rooms;
    private List<User> onlineUsers;

    public RoomData() {
        this.rooms = new LinkedList<>();
        this.onlineUsers = new LinkedList<>();
    }

    public List<User> getOnlineUsers(){
        return this.onlineUsers;
    }
    
    public List<SimpleRoom> getRooms() {
        return this.rooms;
    }
    
    public void removeUser(User user){
        this.onlineUsers.remove(user);
    }

    public void addUser(User user) {
        this.onlineUsers.add(user);
    }
    
    public void removeRoom(SimpleRoom room){
        this.rooms.remove(room);
    }

    public void addRoom(SimpleRoom room) {
        this.rooms.add(room);
    }
}
