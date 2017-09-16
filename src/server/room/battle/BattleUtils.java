/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import java.awt.Dimension;
import java.awt.Point;
import server.actors.Skill;
import server.actors.Spaceship;
import constants.Constants;

/**
 *
 * @author elderjr
 */
public class BattleUtils {
    
    public static final Dimension MAP_SIZE = new Dimension(745, 450);
    
    public static boolean isColision(Spaceship spaceship, Skill skill) {
        int distanceX = Math.abs(spaceship.getLocation().x - skill.getLocation().x);
        int distanceY = Math.abs(spaceship.getLocation().y - skill.getLocation().y);
        return (distanceX < spaceship.getSize().getWidth() / 2 + skill.getSize().getWidth() / 2)
                && (distanceY < spaceship.getSize().getHeight() / 2 + skill.getSize().getHeight() / 2);
    }
    
    public static boolean isOutside(int x, int y){
        return (x > MAP_SIZE.getWidth() || x < 0 
                || y > MAP_SIZE.getHeight() || y < 0);
    }
    
    public static boolean isOutside(Point p){
        return isOutside(p.x, p.y);
    }
    
    
    
}
