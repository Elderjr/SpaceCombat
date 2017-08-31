/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import javafx.scene.canvas.GraphicsContext;
import server.room.SimpleRoom;

/**
 *
 * @author elderjr
 */
public class RoomButton extends Button{
    
    private SimpleRoom room;
    
    public RoomButton(int x, int y,SimpleRoom room,ActionPerfomed action) {
        super(x, y, null, 100, 100, action);
        this.room = room;
    }
    
    
    public SimpleRoom getRoom(){
        return this.room;
    }
    
    public void updateValues(SimpleRoom room){
        this.room.setState(room.getState());
        this.room.setTotalPlayers(room.getTotalPlayers());
    }
    
    @Override
    public void render(GraphicsContext gc){
        super.render(gc);
        gc.setLineWidth(2);
        gc.fillText("ID: " + room.getId(), getX() + 30, getY() + 30);
        gc.strokeText("ID: " + room.getId(), getX() + 30, getY() + 30);
        gc.fillText(room.getTotalPlayers() + "/" + (room.getMaxPlayersPerTeam() * 2), getX() + 60, getY() + 30);
        gc.strokeText(room.getTotalPlayers() + "/" + (room.getMaxPlayersPerTeam() * 2), getX() + 60, getY() + 30);
        gc.fillText("Time: " + room.getMathTime(), getX() + 90, getY() + 30);
        gc.strokeText("Time: " + room.getMathTime(), getX() + 90, getY() + 30);
        gc.fillText("Name: " + room.getName(), getX() + 30, getY() + 60);
        gc.strokeText("Name: " + room.getName(), getX() + 30, getY() + 60);
        if (room.getState() == 0) {
            gc.fillText("WAITING", getX() + 30, getY() + 90);
            gc.strokeText("WAITING", getX() + 30, getY() + 90);
        } else if (room.getState() == 1) {
            gc.fillText("PLAYING", getX() + 30, getY() + 90);
            gc.strokeText("PLAYING", getX() + 30, getY() + 90);
        } else if (room.getState() == 2) {
            gc.fillText("DONE", getX() + 30, getY() + 90);
            gc.strokeText("DONE", getX() + 30, getY() + 90);
        }
    }
}
