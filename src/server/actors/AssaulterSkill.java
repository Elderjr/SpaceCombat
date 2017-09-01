package server.actors;

import java.awt.Dimension;
import java.awt.Point;

import client.commands.ClientCommands;
import server.room.battle.BattleListener;
import server.serverConstants.ServerConstants;

public class AssaulterSkill extends Skill implements Moviment {

    private static final Dimension SIZE = new Dimension(32, 32);
    private static final int SPEED = 10;
    private static final int DAMAGE = 15;

    public static final int COOLDOWN = 4 * 1000; //4s

    public AssaulterSkill(BattleListener room, Point location, Spaceship source, int currentDirection) {
        super(room, location, SIZE, source, DAMAGE, currentDirection, ActorsTypes.ASSAULTER_SKILL);
    }

    @Override
    public boolean update() {
        move(getCurrentDirection());
        return (getLocation().x > ServerConstants.MAP_WIDTH || getLocation().x < 0 
                || getLocation().y > ServerConstants.MAP_HEIGHT || getLocation().y < 0);
    }

    @Override
    public void move(int direction) {
        int x = this.getLocation().x;
        int y = this.getLocation().y;
        if (direction == ClientCommands.UP) {
            y -= SPEED;
        } else if (direction == ClientCommands.UP_LEFT) {
            y -= SPEED;
            x -= SPEED;
        } else if (direction == ClientCommands.UP_RIGHT) {
            y -= SPEED;
            x += SPEED;
        } else if (direction == ClientCommands.DOWN) {
            y += SPEED;
        } else if (direction == ClientCommands.DOWN_LEFT) {
            y += SPEED;
            x -= SPEED;
        } else if (direction == ClientCommands.DOWN_RIGHT) {
            y += SPEED;
            x += SPEED;
        } else if (direction == ClientCommands.LEFT) {
            x -= SPEED;
        } else if (direction == ClientCommands.RIGHT) {
            x += SPEED;
        }
        updateLocation(x, y);
    }

    @Override
    public boolean onColision(Spaceship spaceship) {
        if (spaceship.getTeam() != this.getTeam()) {
            spaceship.receiveDamage(this);
            return true;
        }
        return false;
    }
}
