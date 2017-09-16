package server.actors;

import java.awt.Point;

import constants.Constants;
import server.room.battle.BattleListener;
import server.data.User;

public class Supporter extends Spaceship {

    private static final int MOVIMENT_SPEED = 7;
    private static final int MAX_HP = 100;

    public Supporter(BattleListener room, Position location, int team, int initialDirection, User user) {
        super(room, location, team, Constants.SPACESHIP_SUPPORTER, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public Skill useSkill() {
        if (canUseSkill()) {
            double x = this.getLocation().getX();
            double y = this.getLocation().getY();
            if (getCurrentDirection() == Constants.UP) {
                y += this.getSize().getHeight();
            } else if (getCurrentDirection() == Constants.DOWN) {
                y -= this.getSize().getHeight();
            } else if (getCurrentDirection() == Constants.LEFT) {
                x += this.getSize().getWidth();
            } else if (getCurrentDirection() == Constants.RIGHT) {
                x -= this.getSize().getWidth();
            } else if (getCurrentDirection() == Constants.UP_LEFT) {
                x += this.getSize().getWidth();
                y += this.getSize().getHeight();
            } else if (getCurrentDirection() == Constants.UP_RIGHT) {
                y += this.getSize().getHeight();
                x -= this.getSize().getWidth();
            } else if (getCurrentDirection() == Constants.DOWN_RIGHT) {
                y -= this.getSize().getHeight();
                x -= this.getSize().getWidth();
            } else if (getCurrentDirection() == Constants.DOWN_LEFT) {
                x += this.getSize().getWidth();
                y -= this.getSize().getHeight();
            }
            SupporterSkill skill = new SupporterSkill(getBattleListener(), new Position(x, y), this);
            this.skillFired = System.currentTimeMillis();
            return skill;
        }
        return null;
    }

    @Override
    public boolean canUseSkill() {
        return System.currentTimeMillis() - skillFired >= SupporterSkill.COOLDOWN;
    }

}
