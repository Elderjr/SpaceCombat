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
import server.data.LobbyUser;
import server.serverConstants.ServerConstants;

public class BattleManager implements BattleListener {

    private final long startedAt;
    private final long matchTime;
    private final HashMap<Long, BattleUser> battleUsers;
    private final BattleStatistic battleStatistic;
    private final BattleActorsManager actorsManager;
    private long globalActorId;
    private long currentMatchTime;
    private int blueTeamPoint;
    private int redTeamPoint;

    public BattleManager(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam, long matchTime) {
        this.actorsManager = new BattleActorsManager();
        this.battleStatistic = new BattleStatistic();
        this.battleUsers = new HashMap<>();
        this.globalActorId = 0;
        this.blueTeamPoint = 0;
        this.redTeamPoint = 0;
        initTeamSpaceships(blueTeam, redTeam);
        this.startedAt = System.currentTimeMillis();
        this.matchTime = matchTime;
        this.currentMatchTime = matchTime;
    }

    private void initTeamSpaceships(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        Random r = new Random();
        for (Entry<Long, LobbyUser> entry : blueTeam.entrySet()) {
            Point location = new Point(150, r.nextInt(520) + 30);
            Spaceship spaceship = SpaceshipFactory.createSpaceship(
                    entry.getValue().getSpaceshipSelected(), this,
                    location, ServerConstants.BLUE_TEAM, ClientCommands.RIGHT, entry.getValue().getUser());
            this.battleUsers.put(entry.getKey(), new BattleUser(spaceship));
            this.battleStatistic.addUser(entry.getValue().getUser(), entry.getValue().getSpaceshipSelected(), ServerConstants.BLUE_TEAM);
            this.actorsManager.addSpaceship(spaceship);
            location.y += 50;
        }

        for (Entry<Long, LobbyUser> entry : redTeam.entrySet()) {
            Point location = new Point(650, r.nextInt(520) + 30);
            Spaceship spaceship = SpaceshipFactory.createSpaceship(
                    entry.getValue().getSpaceshipSelected(), this,
                    location, ServerConstants.RED_TEAM, ClientCommands.LEFT, entry.getValue().getUser());
            this.battleUsers.put(entry.getKey(), new BattleUser(spaceship));
            this.battleStatistic.addUser(entry.getValue().getUser(), entry.getValue().getSpaceshipSelected(), ServerConstants.RED_TEAM);
            this.actorsManager.addSpaceship(spaceship);
            location.y += 50;
        }
    }

    public void move(long userId, int direction) {
        if (this.battleUsers.containsKey(userId)) {
            this.battleUsers.get(userId).setDirectionToMove(direction);
        }
    }

    public void shoot(long userId) {
        if (this.battleUsers.containsKey(userId)) {
            this.battleUsers.get(userId).setShoot(true);
        }
    }

    public void useSkill(long userId) {
        if (this.battleUsers.containsKey(userId)) {
            this.battleUsers.get(userId).setUseSkill(true);
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
        if (this.battleUsers.containsKey(userId)) {
            Spaceship spaceship = this.battleUsers.get(userId).getSpaceship();
            this.actorsManager.removeSpaceship(spaceship);
            this.battleUsers.remove(userId);
        }
    }

    @Override
    public long createsActorId() {
        return this.globalActorId++;
    }

    public BattleData getBattleData(long userId) {
        if (this.battleUsers.containsKey(userId)) {
            Spaceship spaceship = this.battleUsers.get(userId).getSpaceship();
            return new BattleData(this.actorsManager.getSimpleActors(), blueTeamPoint, redTeamPoint, currentMatchTime,
                    spaceship.canUseShot(), spaceship.canUseSkill(), spaceship.getHP());
        }
        return null;
    }

    public BattleStatistic getBattleStatistic() {
        return this.battleStatistic;
    }

    public void processActions() {
        for (BattleUser battleUser : this.battleUsers.values()) {
            if (battleUser.isShoot()) {
                Skill skill = battleUser.getSpaceship().shoot();
                if (skill != null) {
                    this.actorsManager.addSkill(skill);
                }
                battleUser.setShoot(false);
            }
            if (battleUser.isUseSkill()) {
                Skill skill = battleUser.getSpaceship().useSkill();
                if (skill != null) {
                    this.actorsManager.addSkill(skill);
                }
                battleUser.setUseSkill(false);
            }
            if (battleUser.getDirectionToMove() != -1) {
                battleUser.getSpaceship().move(battleUser.getDirectionToMove());
                battleUser.setDirectionToMove(-1);
            }

        }
    }

    public boolean update() {
        if (this.currentMatchTime > 0) {
            this.currentMatchTime = matchTime - (System.currentTimeMillis() - this.startedAt);
            if (this.currentMatchTime > 0) {
                this.processActions();
                this.actorsManager.update();
                return false;
            } else if (this.blueTeamPoint > this.redTeamPoint) {
                this.battleStatistic.setWinner(ServerConstants.BLUE_TEAM);
                return true;
            } else if (this.redTeamPoint > this.blueTeamPoint) {
                this.battleStatistic.setWinner(ServerConstants.RED_TEAM);
                return true;
            } else {
                this.battleStatistic.setWinner(ServerConstants.DRAW);
                return true;
            }
        }
        return false;
    }
}
