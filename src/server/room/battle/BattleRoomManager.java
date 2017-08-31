package server.room.battle;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map.Entry;

import client.commands.ClientCommands;
import java.util.Random;
import server.actors.Skill;
import server.actors.Spaceship;
import server.actors.SpaceshipFactory;
import server.data.BattleData;
import server.room.Room;
import server.room.lobby.LobbyUser;
import server.serverConstants.ServerConstants;

public class BattleRoomManager implements BattleListener {

    private Room room;
    private long globalActorId;
    private HashMap<Long, Spaceship> spaceships;
    private BattleStatistic battleStatistic;
    private ActorsManager actorsManager;
    private final long startedAt;
    private long currentMatchTime;
    private int blueTeamPoint;
    private int redTeamPoint;

    public BattleRoomManager(Room room, HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        room.setState(1);
        this.room = room;
        this.globalActorId = 0;
        this.blueTeamPoint = 0;
        this.redTeamPoint = 0;
        this.actorsManager = new ActorsManager();
        this.battleStatistic = new BattleStatistic();
        initTeamSpaceships(blueTeam, redTeam);
        this.startedAt = System.currentTimeMillis();
        this.currentMatchTime = room.getMatchTime();
    }

    private void initTeamSpaceships(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        this.spaceships = new HashMap<>();
        Random r = new Random();
        for (Entry<Long, LobbyUser> entry : blueTeam.entrySet()) {
            Point location = new Point(150, r.nextInt(520) + 30);
            Spaceship spaceship = SpaceshipFactory.createSpaceship(
                    entry.getValue().getSpaceshipSelected(), this,
                    location, ServerConstants.BLUE_TEAM, ClientCommands.RIGHT, entry.getValue().getUser());
            this.actorsManager.addSpaceship(spaceship);
            this.spaceships.put(entry.getKey(), spaceship);
            this.battleStatistic.addUser(entry.getValue().getUser(), entry.getValue().getSpaceshipSelected(), ServerConstants.BLUE_TEAM);
            location.y += 50;
        }

        for (Entry<Long, LobbyUser> entry : redTeam.entrySet()) {
            Point location = new Point(650, r.nextInt(520) + 30);
            Spaceship spaceship = SpaceshipFactory.createSpaceship(
                    entry.getValue().getSpaceshipSelected(), this,
                    location, ServerConstants.RED_TEAM, ClientCommands.LEFT, entry.getValue().getUser());
            this.actorsManager.addSpaceship(spaceship);
            this.spaceships.put(entry.getKey(), spaceship);
            this.battleStatistic.addUser(entry.getValue().getUser(), entry.getValue().getSpaceshipSelected(), ServerConstants.RED_TEAM);
            location.y += 50;
        }
    }

    public void move(long userId, int direction) {
        if (this.spaceships.containsKey(userId)) {
            this.spaceships.get(userId).move(direction);
        }
    }

    public void useShot(long userId) {
        if (this.spaceships.containsKey(userId)) {
            Spaceship spaceship = this.spaceships.get(userId);
            spaceship.setUseShotToTrue();
        }
    }

    public void useSkill(long userId) {
        if (this.spaceships.containsKey(userId)) {
            Spaceship spaceship = this.spaceships.get(userId);
            spaceship.setUseSkillToTrue();
        }
    }

    @Override
    public void damageNotification(Spaceship target, Spaceship source, int damage) {
        this.battleStatistic.incrementDamageTaken(target.getPilot(), damage);
        this.battleStatistic.incrementDamage(source.getPilot(), damage);
    }

    @Override
    public void healNotification(Spaceship target, Spaceship source, int heal) {
        this.battleStatistic.incrementHealTaken(target.getPilot(), heal);
        this.battleStatistic.incrementHeal(source.getPilot(), heal);
    }

    @Override
    public void deathNotification(Spaceship dead, Spaceship killer) {
        this.battleStatistic.incrementDeath(dead.getPilot());
        this.battleStatistic.incrementKills(killer.getPilot());
        Random r = new Random();
        if (dead.getTeam() == ServerConstants.BLUE_TEAM) {
            dead.updateLocation(150, r.nextInt(520) + 30);
            redTeamPoint++;
        } else {
            dead.updateLocation(650, r.nextInt(520) + 30);
            blueTeamPoint++;
        }
        dead.restoreHP();
    }

    @Override
    public void removeUser(long userId) {
        if (this.spaceships.containsKey(userId)) {
            Spaceship spaceship = this.spaceships.get(userId);
            this.actorsManager.removeSpaceship(spaceship);
        }
    }

    @Override
    public void addSkill(Skill skill) {
        this.actorsManager.addSkill(skill);
    }

    @Override
    public void removeSkill(Skill skill) {
        this.actorsManager.removeSkill(skill);
    }

    @Override
    public long createsActorId() {
        return this.globalActorId++;
    }

    public BattleData getBattleData(long userId) {
        Spaceship spaceship = null;
        if (this.spaceships.containsKey(userId)) {
            spaceship = this.spaceships.get(userId);
            return new BattleData(actorsManager.getSimpleActors(), blueTeamPoint, redTeamPoint, currentMatchTime,
                    spaceship.canUseShot(), spaceship.canUseSkill(), spaceship.getHP());
        }
        return null;
    }

    public BattleStatistic getBattleStatistic() {
        return this.battleStatistic;
    }

    public boolean update() {
        if (this.currentMatchTime > 0) {
            this.currentMatchTime = room.getMatchTime() - (System.currentTimeMillis() - this.startedAt);
            this.actorsManager.update();
            if (this.currentMatchTime <= 0) {
                if (this.blueTeamPoint > this.redTeamPoint) {
                    this.battleStatistic.setWinner(ServerConstants.BLUE_WINNER);
                } else if (this.redTeamPoint > this.blueTeamPoint) {
                    this.battleStatistic.setWinner(ServerConstants.RED_WINNER);
                } else {
                    this.battleStatistic.setWinner(ServerConstants.DRAW);
                }
                return true;
            }
        }
        return false;
    }

}
