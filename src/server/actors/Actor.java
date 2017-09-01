package server.actors;

import server.data.SimpleActor;
import java.awt.Dimension;
import java.awt.Point;
import server.room.battle.BattleListener;

public abstract class Actor {

    private final BattleListener room;
    private final Point location;
    private final Dimension size;
    private final String actorType;
    private final long id;
    private final int team;
    private final SimpleActor simpleActor;
    private int currentDirection;

    public Actor(BattleListener room, Point location, Dimension size, int team, String actorType, int currentDirection) {
        this.room = room;
        this.location = location;
        this.size = size;
        this.team = team;
        this.actorType = actorType;
        this.id = room.createsActorId();
        this.currentDirection = currentDirection;
        this.simpleActor = new SimpleActor(location, size, id, actorType, currentDirection, team);
    }

    public abstract boolean update();

    public long getId() {
        return this.id;
    }

    public SimpleActor getSimpleActor() {
        return this.simpleActor;
    }

    public Point getLocation() {
        return this.location;
    }

    protected int getCurrentDirection() {
        return this.currentDirection;
    }

    public Dimension getSize() {
        return this.size;
    }

    public int getTeam() {
        return this.team;
    }

    protected BattleListener getBattleListener() {
        return this.room;
    }

    public void updateLocation(int x, int y) {
        this.location.setLocation(x, y);
    }

    protected void setCurrentDirection(int direction) {
        this.currentDirection = direction;
        this.simpleActor.setDirection(direction);
    }

}
