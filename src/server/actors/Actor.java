package server.actors;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Dimension2D;

import server.room.Room;
import server.room.battle.BattleListener;

public abstract class Actor {

    private BattleListener room;
    private Point location;
    private Dimension size;
    private SimpleActor simpleActor;
    private long id;
    private int team;
    private int currentDirection;

    public Actor(BattleListener room, Point location, Dimension size, int team, String actorType, int currentDirection) {
        this.room = room;
        this.location = location;
        this.size = size;
        this.team = team;
        this.id = room.createsActorId();
        this.currentDirection = currentDirection;
        this.simpleActor = new SimpleActor(location, size, id, actorType, currentDirection, team);
    }

    public abstract void update();

    public long getId() {
        return this.id;
    }

    public SimpleActor getSimpleActor() {
        return this.simpleActor;
    }
    
    public Point getLocation() {
        return this.location;
    }

    public void updateLocation(int x, int y) {
        this.location.x = x;
        this.location.y = y;
    }

    protected int getCurrentDirection() {
        return this.currentDirection;
    }

    public Dimension getSize() {
        return this.size;
    }

    protected void setCurrentDirection(int direction) {        
        this.currentDirection = direction;
        this.simpleActor.setDirection(direction);
    }

    public void setTeam(int team) {
        this.team = team;
        this.simpleActor.setTeam(team);
    }

    public int getTeam() {
        return this.team;
    }

    protected BattleListener getBattleListener() {
        return this.room;
    }
}
