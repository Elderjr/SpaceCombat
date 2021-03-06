/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.input.ExternalFileLoader;
import constants.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.room.SimpleRoom;

/**
 *
 * @author elderjr
 */
public class RoomButton extends Button{
    
    private static final Font TITLE_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
         FontPosture.REGULAR, 20);
    private static final Font CHARACTER_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
         FontPosture.REGULAR, 25);
    private static final Image ROOM_BUTTON = ExternalFileLoader.getInstance().getImage("room.png");
    private final long time;
    private final SimpleRoom room;
    
    public RoomButton(double x, double y, SimpleRoom room, ActionPerfomed action) {
        super(x, y, action);
        this.setSize(ROOM_BUTTON.getWidth(), ROOM_BUTTON.getHeight());
        this.room = room;
        this.time = room.getMathTime() / 60000;
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
        gc.drawImage(ROOM_BUTTON, getX(), getY());
        gc.setFont(TITLE_FONT);
        gc.fillText("Room #" + room.getId() + " - " + room.getName(), getX() + 10, getY() + 35);
        gc.setFont(CHARACTER_FONT);
        gc.fillText(room.getTotalPlayers() + " / " + (room.getMaxPlayersPerTeam() * 2), getX() + 290, getY() + 45);
        gc.fillText(this.time+":00", getX() + 315, getY() + 85);
        if (room.getState() == Constants.WAITING && room.getTotalPlayers() != room.getMaxPlayersPerTeam() * 2) {
            gc.fillText("WAITING", getX() + 15, getY() + 90);
            gc.fillRect(getX() + 140, getY() + 85, 5, 5);
            gc.fillRect(getX() + 150, getY() + 85, 5, 5);
            gc.fillRect(getX() + 160, getY() + 85, 5, 5);
        } else if(room.getState() == Constants.WAITING && room.getTotalPlayers() == room.getMaxPlayersPerTeam() * 2){
            gc.fillText("FULL", getX() + 15, getY() + 90);
        } else if (room.getState() == Constants.PLAYING) {
            gc.fillText("PLAYING", getX() + 15, getY() + 90);
        }
    }
}
