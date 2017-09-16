package server.room.battle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import server.data.SimpleActor;
import server.actors.Skill;
import server.actors.Spaceship;

public class BattleActorsManager {

    private final List<Spaceship> spaceships;
    private final List<Skill> skills;
    private final HashMap<Long, SimpleActor> simpleActors;

    public BattleActorsManager() {
        this.spaceships = new LinkedList<>();
        this.skills = new LinkedList<>();
        this.simpleActors = new HashMap<>();
    }

    public HashMap<Long, SimpleActor> getSimpleActors() {
        return this.simpleActors;
    }

    public void addSpaceship(Spaceship spaceship) {
        this.spaceships.add(spaceship);
        this.simpleActors.put(spaceship.getId(), spaceship.getSimpleActor());
    }

    public void removeSpaceship(Spaceship spaceship) {
        this.spaceships.remove(spaceship);
        this.simpleActors.remove(spaceship.getId());
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
        this.simpleActors.put(skill.getId(), skill.getSimpleActor());
    }

    private void checkColisions() {
        Skill skill;
        for (Spaceship spaceship : this.spaceships) {
            Iterator<Skill> iterator = this.skills.iterator();
            while (iterator.hasNext()) {
                skill = iterator.next();
                if (ColisionChecker.isColision(spaceship, skill) && skill.onColision(spaceship)) {
                    this.simpleActors.remove(skill.getId());
                    iterator.remove();
                }
            }
        }
    }

    public void update(long time) {
        checkColisions();
        for (Spaceship spaceship : spaceships) {
            spaceship.update(time);
        }
        Iterator<Skill> iterator = this.skills.iterator();
        Skill skill;
        while(iterator.hasNext()){
            skill = iterator.next();
            if(skill.update(time)){
                this.simpleActors.remove(skill.getId());
                iterator.remove();
            }
        }
        
    }
}
