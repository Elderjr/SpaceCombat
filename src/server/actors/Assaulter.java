package server.actors;

import java.awt.Point;

import constants.Constants;
import server.room.battle.BattleListener;
import server.data.User;

public class Assaulter extends Spaceship {

    private static final int MOVIMENT_SPEED = 7;
    private static final int MAX_HP = 100;

    public Assaulter(BattleListener room, Point location, int team, int initialDirection, User user) {
        super(room, location, team, Constants.SPACESHIP_ASSAULTER, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public Skill useSkill() {
        if (canUseSkill()) {
            Point skillLocation = new Point(this.getLocation().x, this.getLocation().y);
            if (getCurrentDirection() == Constants.UP) {
                skillLocation.y -= this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.DOWN) {
                skillLocation.y += this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.LEFT) {
                skillLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.RIGHT) {
                skillLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_LEFT) {
                skillLocation.y -= this.getSize().getHeight() / 2;
                skillLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_RIGHT) {
                skillLocation.y -= this.getSize().getHeight() / 2;
                skillLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_RIGHT) {
                skillLocation.y += this.getSize().getHeight() / 2;
                skillLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_LEFT) {
                skillLocation.y += this.getSize().getHeight() / 2;
                skillLocation.x -= this.getSize().getWidth() / 2;
            }
            AssaulterSkill skill = new AssaulterSkill(getBattleListener(), skillLocation, this, getCurrentDirection());
            this.skillFired = System.currentTimeMillis();
            return skill;
        }else{
            return null;
        }
    }

    @Override
    public boolean canUseSkill() {
        return System.currentTimeMillis() - skillFired >= AssaulterSkill.COOLDOWN;
    }

}
