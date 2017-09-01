package server.actors;

import java.awt.Dimension;
import java.awt.Point;

import server.room.battle.BattleListener;
import server.serverConstants.ServerConstants;

public class RaptorSkill extends Skill {

    private static final Dimension SIZE = new Dimension(48, 48);
    private static final int DAMAGE = 25;
    public static final int COOLDOWN = 10 * 1000;

    public RaptorSkill(BattleListener room, Point location, Spaceship source) {
        super(room, location, SIZE, source, DAMAGE, 0, ActorsTypes.RAPTOR_SKILL);
    }

    @Override
    public boolean update() {
        return (getLocation().x > ServerConstants.MAP_WIDTH || getLocation().x < 0 
                || getLocation().y > ServerConstants.MAP_HEIGHT || getLocation().y < 0);
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
