/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import java.awt.Dimension;
import server.actors.Skill;
import server.actors.Spaceship;
import server.actors.Position;

/**
 *
 * @author elderjr
 */
public class BattleUtils {
    
    public static final Dimension MAP_SIZE = new Dimension(745, 450);
    
    public static boolean isColision(Spaceship spaceship, Skill skill) {
        double distanceX = Math.abs(spaceship.getLocation().getX() - skill.getLocation().getX());
        double distanceY = Math.abs(spaceship.getLocation().getY() - skill.getLocation().getY());
        return (distanceX < spaceship.getSize().getWidth() / 2 + skill.getSize().getWidth() / 2)
                && (distanceY < spaceship.getSize().getHeight() / 2 + skill.getSize().getHeight() / 2);
    }
    
    public static boolean isOutside(double x, double y){
        return (x > MAP_SIZE.getWidth() || x < 0 
                || y > MAP_SIZE.getHeight() || y < 0);
    }
    
    public static boolean isOutside(Position p){
        return isOutside(p.getX(), p.getY());
    }
    
    
    
}
