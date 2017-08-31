package server.room.battle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import server.actors.SimpleActor;
import server.actors.Skill;
import server.actors.Spaceship;

public class ActorsManager {

    private List<Spaceship> spaceships;
    private List<Spaceship> spaceshipsToRemove;
    private List<Skill> skills;
    private List<Skill> skillsToRemove;
    private List<Skill> skillsToAdd;
    private HashMap<Long, SimpleActor> simpleActors;

    public ActorsManager() {
        this.spaceships = new LinkedList<>();
        this.spaceshipsToRemove = new LinkedList<>();
        this.skills = new LinkedList<>();
        this.skillsToRemove = new LinkedList<>();
        this.skillsToAdd = new LinkedList<>();
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
        this.spaceshipsToRemove.add(spaceship);
    }

    public void addSkill(Skill skill) {
        this.skillsToAdd.add(skill);
    }

    public void removeSkill(Skill skill) {
        this.skillsToRemove.add(skill);
    }

    public void update() {
        ColisionChecker.colisionChecker(this.spaceships, this.skills);
        if (spaceshipsToRemove.size() > 0) {
            for (Spaceship spaceship : this.spaceshipsToRemove) {
                this.spaceships.remove(spaceship);
                this.simpleActors.remove(spaceship.getId());
            }
            spaceshipsToRemove.clear();
        }

        if (skillsToRemove.size() > 0) {
            for (Skill skill : this.skillsToRemove) {
                this.skills.remove(skill);
                this.simpleActors.remove(skill.getId());
            }
            skillsToRemove.clear();
        }

        if (skillsToAdd.size() > 0) {
            for (Skill skill : this.skillsToAdd) {
                this.skills.add(skill);
                this.simpleActors.put(skill.getId(), skill.getSimpleActor());
            }
            skillsToAdd.clear();
        }
        for (Spaceship spaceship : spaceships) {
            spaceship.update();
        }
        for (Skill skill : skills) {
            skill.update();
        }
    }
}
