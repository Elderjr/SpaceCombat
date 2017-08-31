package server.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import server.room.SimpleRoom;

public class RoomData implements Serializable{

	private List<SimpleRoom> rooms;
	
	public RoomData(){
		this.rooms = new LinkedList<>();
	}
	
	public List<SimpleRoom> getRooms(){
		return this.rooms;
	}
	
	public void addRoom(SimpleRoom room){
		this.rooms.add(room);
	}
}
