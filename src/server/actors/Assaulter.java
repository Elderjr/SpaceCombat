package server.actors;

import java.awt.Point;

import client.commands.ClientCommands;
import server.room.Room;
import server.room.battle.BattleListener;
import server.user.SimpleUser;

public class Assaulter extends Spaceship {

    private static final int MOVIMENT_SPEED = 7;
    private static final int MAX_HP = 100;

    public Assaulter(BattleListener room, Point location, int team, int initialDirection, SimpleUser user) {
        super(room, location, team, ActorsTypes.SPACESHIP_ASSAULTER, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public void useSkill() {
        if (canUseSkill()) {
            Point skillLocation = new Point(this.getLocation().x, this.getLocation().y);
            if (getCurrentDirection() == ClientCommands.UP) {
                skillLocation.y -= this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == ClientCommands.DOWN) {
                skillLocation.y += this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == ClientCommands.LEFT) {
                skillLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.RIGHT) {
                skillLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.UP_LEFT) {
                skillLocation.y -= this.getSize().getHeight() / 2;
                skillLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.UP_RIGHT) {
                skillLocation.y -= this.getSize().getHeight() / 2;
                skillLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.DOWN_RIGHT) {
                skillLocation.y += this.getSize().getHeight() / 2;
                skillLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.DOWN_LEFT) {
                skillLocation.y += this.getSize().getHeight() / 2;
                skillLocation.x -= this.getSize().getWidth() / 2;
            }
            AssaulterSkill skill = new AssaulterSkill(getBattleListener(), skillLocation, this, getCurrentDirection());
            getBattleListener().addSkill(skill);
            this.skillFired = System.currentTimeMillis();
        }
    }

    @Override
    public boolean canUseSkill() {
        return System.currentTimeMillis() - skillFired >= AssaulterSkill.COOLDOWN;
    }

}
