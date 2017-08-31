package server.room.battle;

import java.util.List;

import server.actors.Skill;
import server.actors.Spaceship;

public class ColisionChecker {

    public static void colisionChecker(List<Spaceship> spaceships, List<Skill> skills) {
        for (Spaceship spaceship : spaceships) {
            for (Skill skill : skills) {
                if (isColision(spaceship, skill)) {
                    skill.onColision(spaceship);
                }
            }
        }
    }

    private static boolean isColision(Spaceship spaceship, Skill skill) {
        int distanceX = Math.abs(spaceship.getLocation().x - skill.getLocation().x);
        int distanceY = Math.abs(spaceship.getLocation().y - skill.getLocation().y);
        return (distanceX < spaceship.getSize().getWidth() / 2 + skill.getSize().getWidth() / 2)
                && (distanceY < spaceship.getSize().getHeight() / 2 + skill.getSize().getHeight() / 2);
    }
}
