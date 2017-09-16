package server.actors;


import constants.Constants;
import server.room.battle.BattleListener;
import server.data.User;

public class Assaulter extends Spaceship {

    private static final int MOVIMENT_SPEED = 7;
    private static final int MAX_HP = 100;

    public Assaulter(BattleListener room, Position location, int team, int initialDirection, User user) {
        super(room, location, team, Constants.SPACESHIP_ASSAULTER, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public Skill useSkill() {
        if (canUseSkill()) {
            double x = this.getLocation().getX();
            double y = this.getLocation().getY();
            if (getCurrentDirection() == Constants.UP) {
                y -= this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.DOWN) {
                y += this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.LEFT) {
                x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.RIGHT) {
                x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_LEFT) {
                y -= this.getSize().getHeight() / 2;
                x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_RIGHT) {
                y -= this.getSize().getHeight() / 2;
                x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_RIGHT) {
                y += this.getSize().getHeight() / 2;
                x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_LEFT) {
                y += this.getSize().getHeight() / 2;
                x -= this.getSize().getWidth() / 2;
            }
            AssaulterSkill skill = new AssaulterSkill(getBattleListener(), new Position(x, y), this, getCurrentDirection());
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
