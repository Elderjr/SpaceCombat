package server.actors;

import java.awt.Dimension;
import java.awt.Point;
import server.room.battle.BattleListener;

public abstract class Skill extends Actor {

    private int damage;
    private Spaceship source;

    public Skill(BattleListener room, Position location, Dimension size, Spaceship source, int damage, int currentDirection, String actorType) {
        super(room, location, size, source.getTeam(), actorType, currentDirection);
        this.damage = damage;
        this.source = source;
    }

    public abstract boolean onColision(Spaceship spaceship);

    public Spaceship getSource(){
        return this.source;
    }
    
    public int getDamage() {
        return this.damage;
    }

}
