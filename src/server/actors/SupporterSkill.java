package server.actors;

import java.awt.Dimension;
import java.awt.Point;
import server.room.battle.BattleListener;
import constants.Constants;
import server.room.battle.BattleUtils;

public class SupporterSkill extends Skill {

    private static final Dimension SIZE = new Dimension(48, 48);
    private static final int DAMAGE = 10;
    public static final int COOLDOWN = 4 * 1000; //5s

    public SupporterSkill(BattleListener room, Position location, Spaceship source) {
        super(room, location, SIZE, source, DAMAGE, 0, Constants.SUPPORTER_SKILL);
    }

    @Override
    public boolean update(long time) {
        return BattleUtils.isOutside(getLocation());
    }

    @Override
    public boolean onColision(Spaceship spaceship) {
        if (spaceship.getTeam() == this.getTeam()) {
            spaceship.receiveHeal(this);            
            return true;
        }
        return false;
    }
}
