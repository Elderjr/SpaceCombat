package server.actors;

import java.awt.Dimension;
import java.awt.Point;


import server.room.battle.BattleListener;
import constants.Constants;
import server.room.battle.BattleUtils;

public class Shot extends Skill implements Moviment {

    public static final Dimension SIZE = new Dimension(32, 32);
    private static final int SPEED = 10;
    private static final int DAMAGE = 5; //5
    public static final int COOLDOWN = 400; //400ms

    public Shot(BattleListener room, Position location, Spaceship source, int currentDirection) {
        super(room, location, SIZE, source, DAMAGE, currentDirection, Constants.SHOT);
    }

    @Override
    public boolean update(long time) {
        move(getCurrentDirection(), SPEED * time);
        return BattleUtils.isOutside(getLocation());
    }

    @Override
    public void move(int direction, double speed) {
        double x = this.getLocation().getX();
        double y = this.getLocation().getY();
        if (direction == Constants.UP) {
            y -= SPEED;
        } else if (direction == Constants.UP_LEFT) {
            y -= SPEED;
            x -= SPEED;
        } else if (direction == Constants.UP_RIGHT) {
            y -= SPEED;
            x += SPEED;
        } else if (direction == Constants.DOWN) {
            y += SPEED;
        } else if (direction == Constants.DOWN_LEFT) {
            y += SPEED;
            x -= SPEED;
        } else if (direction == Constants.DOWN_RIGHT) {
            y += SPEED;
            x += SPEED;
        } else if (direction == Constants.LEFT) {
            x -= SPEED;
        } else if (direction == Constants.RIGHT) {
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
