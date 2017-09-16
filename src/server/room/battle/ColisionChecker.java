package server.room.battle;

import server.actors.Skill;
import server.actors.Spaceship;

public class ColisionChecker {

    public static boolean isColision(Spaceship spaceship, Skill skill) {
        double distanceX = Math.abs(spaceship.getLocation().getX() - skill.getLocation().getX());
        double distanceY = Math.abs(spaceship.getLocation().getY() - skill.getLocation().getY());
        return (distanceX < spaceship.getSize().getWidth() / 2 + skill.getSize().getWidth() / 2)
                && (distanceY < spaceship.getSize().getHeight() / 2 + skill.getSize().getHeight() / 2);
    }
}
