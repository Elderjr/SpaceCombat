package server.actors;

import java.awt.Point;

import client.commands.ClientCommands;
import server.room.battle.BattleListener;
import server.user.SimpleUser;

public class Raptor extends Spaceship {

    private static final int MOVIMENT_SPEED = 9;
    private static final int MAX_HP = 65;

    public Raptor(BattleListener room, Point location, int team, int initialDirection, SimpleUser user) {
        super(room, location, team, ActorsTypes.SPACESHIP_RAPTOR, MAX_HP, MOVIMENT_SPEED, initialDirection, user);
    }

    @Override
    public void useSkill() {

        Point skillLocation = new Point(this.getLocation().x, this.getLocation().y);
        if (this.getCurrentDirection() == ClientCommands.UP) {
            skillLocation.y += this.getSize().getHeight();
        } else if (this.getCurrentDirection() == ClientCommands.DOWN) {
            skillLocation.y -= this.getSize().getHeight();
        } else if (this.getCurrentDirection() == ClientCommands.LEFT) {
            skillLocation.x += this.getSize().getWidth();
        } else if (this.getCurrentDirection() == ClientCommands.RIGHT) {
            skillLocation.x -= this.getSize().getWidth();
        } else if (this.getCurrentDirection() == ClientCommands.UP_LEFT) {
            skillLocation.x += this.getSize().getWidth();
            skillLocation.y += this.getSize().getHeight();
        } else if (this.getCurrentDirection() == ClientCommands.UP_RIGHT) {
            skillLocation.y += this.getSize().getHeight();
            skillLocation.x -= this.getSize().getWidth();
        } else if (this.getCurrentDirection() == ClientCommands.DOWN_RIGHT) {
            skillLocation.y -= this.getSize().getHeight();
            skillLocation.x -= this.getSize().getWidth();
        } else if (this.getCurrentDirection() == ClientCommands.DOWN_LEFT) {
            skillLocation.x += this.getSize().getWidth();
            skillLocation.y -= this.getSize().getHeight();
        }
        RaptorSkill skill = new RaptorSkill(getBattleListener(), skillLocation, this);
        getBattleListener().addSkill(skill);
        this.skillFired = System.currentTimeMillis();

    }

    @Override
    public boolean canUseSkill() {
        return System.currentTimeMillis() - skillFired >= RaptorSkill.COOLDOWN;
    }

}
