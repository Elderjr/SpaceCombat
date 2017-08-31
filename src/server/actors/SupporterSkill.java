package server.actors;

import java.awt.Dimension;
import java.awt.Point;

import client.commands.ClientCommands;
import server.room.Room;
import server.room.battle.BattleListener;
import server.serverConstants.ServerConstants;

public class SupporterSkill extends Skill {

    private static final Dimension SIZE = new Dimension(48, 48);
    private static final int DAMAGE = 10;
    public static final int COOLDOWN = 4 * 1000; //5s

    public SupporterSkill(BattleListener room, Point location, Spaceship source) {
        super(room, location, SIZE, source, DAMAGE, 0, ActorsTypes.SUPPORTER_SKILL);
    }

    @Override
    public void update() {

    }

    @Override
    public void onColision(Spaceship spaceship) {
        if (spaceship.getTeam() == this.getTeam()) {
            spaceship.receiveHeal(this);
            getBattleListener().removeSkill(this);
        }
    }
}
