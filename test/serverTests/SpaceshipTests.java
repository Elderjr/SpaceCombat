/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTests;

import constants.Constants;
import java.awt.Point;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import server.actors.Shot;
import server.actors.Skill;
import server.actors.Spaceship;
import server.actors.SpaceshipFactory;
import server.data.LobbyUser;
import server.data.User;
import server.room.Room;
import server.room.battle.BattleListener;
import server.room.battle.BattleManager;

/**
 *
 * @author elderjr
 */
public class SpaceshipTests {
    
    public SpaceshipTests() {
    }
    
    private Spaceship createSpaceship(String type, int team){
        User user = new User(1, "elderjr");
        Room r = new Room(1, 1000, "Room test");
        BattleListener battleListener = new BattleManager(
                new HashMap<Long, LobbyUser>() ,new HashMap<Long, LobbyUser>(), r);
        return SpaceshipFactory.createSpaceship(
                type, battleListener,
                new Point(100,100), team, 
                Constants.RIGHT, user);
    }
    @Test
    public void directionTest(){
        Spaceship spaceship = createSpaceship(Constants.SPACESHIP_ASSAULTER, Constants.BLUE_TEAM);
        assertEquals(Constants.RIGHT, spaceship.getCurrentDirection());
        spaceship.move(Constants.DOWN);
        assertEquals(Constants.DOWN, spaceship.getCurrentDirection());
        spaceship.move(Constants.DOWN_LEFT);
        assertEquals(Constants.DOWN_LEFT, spaceship.getCurrentDirection());
        spaceship.move(Constants.DOWN_RIGHT);
        assertEquals(Constants.DOWN_RIGHT, spaceship.getCurrentDirection());
        spaceship.move(Constants.UP);
        assertEquals(Constants.UP, spaceship.getCurrentDirection());
        spaceship.move(Constants.UP_LEFT);
        assertEquals(Constants.UP_LEFT, spaceship.getCurrentDirection());
        spaceship.move(Constants.UP_RIGHT);
        assertEquals(Constants.UP_RIGHT, spaceship.getCurrentDirection());
        spaceship.move(Constants.LEFT);
        assertEquals(Constants.LEFT, spaceship.getCurrentDirection());
        spaceship.move(Constants.RIGHT);
        assertEquals(Constants.RIGHT, spaceship.getCurrentDirection());
    }
    
    @Test
    public void shootTest(){
        Spaceship spaceship = createSpaceship(Constants.SPACESHIP_ASSAULTER, Constants.BLUE_TEAM);
        Shot shot = spaceship.shoot();
        assertEquals(true, shot != null);
        assertEquals(shot.getCurrentDirection(), spaceship.getCurrentDirection());
        assertEquals(shot.getSource(), spaceship);
        assertEquals(shot.getTeam(), spaceship.getTeam());
    }
    
    @Test
    public void skillTest(){
        Spaceship spaceship = createSpaceship(Constants.SPACESHIP_ASSAULTER, Constants.BLUE_TEAM);
        Skill skill = spaceship.useSkill();
        assertEquals(true, skill != null);
        assertEquals(skill.getCurrentDirection(), spaceship.getCurrentDirection());
        assertEquals(skill.getSource(), spaceship);
        assertEquals(skill.getTeam(), spaceship.getTeam());
        spaceship = createSpaceship(Constants.SPACESHIP_RAPTOR, Constants.BLUE_TEAM);
        skill = spaceship.useSkill();
        assertEquals(true, skill != null);
        assertEquals(skill.getCurrentDirection(), 0);
        assertEquals(skill.getSource(), spaceship);
        assertEquals(skill.getTeam(), spaceship.getTeam());
        spaceship = createSpaceship(Constants.SPACESHIP_SUPPORTER, Constants.BLUE_TEAM);
        skill = spaceship.useSkill();
        assertEquals(true, skill != null);
        assertEquals(skill.getCurrentDirection(), 0);
        assertEquals(skill.getSource(), spaceship);
        assertEquals(skill.getTeam(), spaceship.getTeam());
    }
    
    @Test
    public void receiveDamageTest(){
        Spaceship spaceshipA = createSpaceship(Constants.SPACESHIP_ASSAULTER, Constants.BLUE_TEAM);
        Spaceship spaceshipB = createSpaceship(Constants.SPACESHIP_ASSAULTER, Constants.RED_TEAM);
        Skill shotB = spaceshipB.shoot();
        spaceshipA.receiveDamage(shotB);
        assertEquals(false, spaceshipA.getHP() != spaceshipA.getMaxHP());
        spaceshipA.receiveHeal(shotB);
        assertEquals(true, spaceshipA.getHP() == spaceshipA.getMaxHP());
    }
}
