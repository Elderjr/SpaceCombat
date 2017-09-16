package server.actors;

import java.awt.Point;

import constants.Constants;
import server.room.battle.BattleListener;
import server.data.User;

public class Raptor extends Spaceship {

    private static final double MOVIMENT_SPEED = 9;
    private static final int MAX_HP = 65;

    public Raptor(BattleListener room, Position location, int team, int initialDirection, User user) {
        super(room, location, team, Constants.SPACESHIP_RAPTOR, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public Skill useSkill() {
        if (canUseSkill()) {
            double x = this.getLocation().getX();
            double y = this.getLocation().getY();
            if (this.getCurrentDirection() == Constants.UP) {
                y += this.getSize().getHeight();
            } else if (this.getCurrentDirection() == Constants.DOWN) {
                y -= this.getSize().getHeight();
            } else if (this.getCurrentDirection() == Constants.LEFT) {
                x += this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.RIGHT) {
                x -= this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.UP_LEFT) {
                x += this.getSize().getWidth();
                y += this.getSize().getHeight();
            } else if (this.getCurrentDirection() == Constants.UP_RIGHT) {
                y += this.getSize().getHeight();
                x -= this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.DOWN_RIGHT) {
                y -= this.getSize().getHeight();
                x -= this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.DOWN_LEFT) {
                x += this.getSize().getWidth();
                y -= this.getSize().getHeight();
            }
            RaptorSkill skill = new RaptorSkill(getBattleListener(), new Position(x, y), this);
            this.skillFired = System.currentTimeMillis();
            return skill;
        }
        return null;
    }

    @Override
    public boolean canUseSkill() {
        return System.currentTimeMillis() - skillFired >= RaptorSkill.COOLDOWN;
    }

}
