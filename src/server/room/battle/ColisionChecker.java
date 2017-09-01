package server.room.battle;

import server.actors.Skill;
import server.actors.Spaceship;

public class ColisionChecker {

    public static boolean isColision(Spaceship spaceship, Skill skill) {
        int distanceX = Math.abs(spaceship.getLocation().x - skill.getLocation().x);
        int distanceY = Math.abs(spaceship.getLocation().y - skill.getLocation().y);
        return (distanceX < spaceship.getSize().getWidth() / 2 + skill.getSize().getWidth() / 2)
                && (distanceY < spaceship.getSize().getHeight() / 2 + skill.getSize().getHeight() / 2);
    }
}
