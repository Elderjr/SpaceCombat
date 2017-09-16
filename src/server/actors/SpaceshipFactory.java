package server.actors;

import constants.Constants;
import java.awt.Point;

import server.room.battle.BattleListener;
import server.data.User;


public class SpaceshipFactory {

    public static Spaceship createSpaceship(String type, BattleListener room, Point location, int team, int initialDirection, User user) {
        if (type.equals(Constants.SPACESHIP_ASSAULTER)) {
            return new Assaulter(room, location, team, initialDirection, user);
        } else if (type.equals(Constants.SPACESHIP_RAPTOR)) {
            return new Raptor(room, location, team, initialDirection, user);
        } else if (type.equals(Constants.SPACESHIP_SUPPORTER)) {
            return new Supporter(room, location, team, initialDirection, user);
        }
        return null;
    }
}
