package server.actors;

import java.awt.Point;

import constants.Constants;
import server.room.battle.BattleListener;
import server.data.User;

public class Raptor extends Spaceship {

    private static final int MOVIMENT_SPEED = 9;
    private static final int MAX_HP = 65;

    public Raptor(BattleListener room, Point location, int team, int initialDirection, User user) {
        super(room, location, team, Constants.SPACESHIP_RAPTOR, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public Skill useSkill() {
        if (canUseSkill()) {
            Point skillLocation = new Point(this.getLocation().x, this.getLocation().y);
            if (this.getCurrentDirection() == Constants.UP) {
                skillLocation.y += this.getSize().getHeight();
            } else if (this.getCurrentDirection() == Constants.DOWN) {
                skillLocation.y -= this.getSize().getHeight();
            } else if (this.getCurrentDirection() == Constants.LEFT) {
                skillLocation.x += this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.RIGHT) {
                skillLocation.x -= this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.UP_LEFT) {
                skillLocation.x += this.getSize().getWidth();
                skillLocation.y += this.getSize().getHeight();
            } else if (this.getCurrentDirection() == Constants.UP_RIGHT) {
                skillLocation.y += this.getSize().getHeight();
                skillLocation.x -= this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.DOWN_RIGHT) {
                skillLocation.y -= this.getSize().getHeight();
                skillLocation.x -= this.getSize().getWidth();
            } else if (this.getCurrentDirection() == Constants.DOWN_LEFT) {
                skillLocation.x += this.getSize().getWidth();
                skillLocation.y -= this.getSize().getHeight();
            }
            RaptorSkill skill = new RaptorSkill(getBattleListener(), skillLocation, this);
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
