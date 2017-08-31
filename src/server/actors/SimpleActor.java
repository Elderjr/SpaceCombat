package server.actors;

import java.awt.Point;
import java.awt.geom.Dimension2D;
import java.io.Serializable;

public class SimpleActor implements Serializable {

    private long id;
    private String actorType;
    private Point location;
    private Dimension2D size;
    private int direction;
    private int team;

    public SimpleActor(Point location, Dimension2D size, long id, String actorType, int direction, int team) {
        this.location = location;
        this.size = size;
        this.id = id;
        this.actorType = actorType;
        this.direction = direction;
        this.team = team;
    }

    
    public long getId(){
        return this.id;
    }
    
    public int getTeam(){
        return this.team;
    }
    
    public void setTeam(int team){
        this.team = team;
    }
    
    public String getActorType(){
        return this.actorType;
    }
    
    public Point getLocation(){
        return this.location;
    }
    
    public Dimension2D getSize(){
        return this.size;                
    }
    
    public void setDirection(int direction){
        this.direction = direction;
    }
    
    public int getDirection(){
        return this.direction;
    }
    
    @Override
    public int hashCode() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleActor) {
            SimpleActor a = (SimpleActor) obj;
            return this.id == a.id;
        }
        return false;
    }
}
