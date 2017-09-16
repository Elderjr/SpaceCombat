package server.actors;

import java.awt.Dimension;
import java.awt.Point;

import server.room.battle.BattleListener;
import constants.Constants;
import server.room.battle.BattleUtils;

public class AssaulterSkill extends Skill implements Moviment {

    private static final Dimension SIZE = new Dimension(32, 32);
    private static final int SPEED = 10;
    private static final int DAMAGE = 15;

    public static final int COOLDOWN = 4 * 1000; //4s

    public AssaulterSkill(BattleListener room, Point location, Spaceship source, int currentDirection) {
        super(room, location, SIZE, source, DAMAGE, currentDirection, Constants.ASSAULTER_SKILL);
    }

    @Override
    public boolean update() {
        move(getCurrentDirection());
        return BattleUtils.isOutside(getLocation());
    }

    @Override
    public void move(int direction) {
        int x = this.getLocation().x;
        int y = this.getLocation().y;
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
