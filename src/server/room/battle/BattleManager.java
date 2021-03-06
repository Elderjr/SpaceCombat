package server.room.battle;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import server.actors.Skill;
import server.actors.Spaceship;
import server.actors.SpaceshipFactory;
import server.data.BattleData;
import server.data.LobbyUser;
import constants.Constants;
import server.actors.Position;
import server.room.Room;

public class BattleManager implements BattleListener {

    private final HashMap<Long, BattleUser> battleUsers;
    private final HashMap<Long, BattleData> battleDatas;
    private final BattleStatistic battleStatistic;
    private final BattleActorsManager actorsManager;
    private final long startedAt;
    private final Room room;
    private long globalActorId;
    private long currentMatchTime;

    public BattleManager(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam, Room room) {
        this.actorsManager = new BattleActorsManager();
        this.battleStatistic = new BattleStatistic();
        this.battleUsers = new HashMap<>();
        this.battleDatas = new HashMap<>();
        this.globalActorId = 0;
        this.room = room;
        initTeamSpaceships(blueTeam, redTeam);
        this.startedAt = System.currentTimeMillis();
        this.currentMatchTime = room.getMatchTime();
    }

    private void initTeamSpaceships(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        Random r = new Random();
        for (Entry<Long, LobbyUser> entry : blueTeam.entrySet()) {
            Position position = new Position(150, randomLocation());
            addUser(entry.getValue(), position, Constants.RIGHT, Constants.BLUE_TEAM);
        }

        for (Entry<Long, LobbyUser> entry : redTeam.entrySet()) {
            Position position = new Position(650, randomLocation());
            addUser(entry.getValue(), position, Constants.LEFT, Constants.RED_TEAM);
        }
    }

    private void addUser(LobbyUser lobbyUser, Position location, int direction, int team) {
        Spaceship spaceship = SpaceshipFactory.createSpaceship(
                lobbyUser.getSpaceshipSelected(), this,
                location, team, direction, lobbyUser.getUser());
        this.battleUsers.put(lobbyUser.getUser().getId(), new BattleUser(spaceship));
        this.battleDatas.put(lobbyUser.getUser().getId(), new BattleData(this.actorsManager.getSimpleActors(), this.room.getMatchTime(), spaceship.getMaxHP()));
        this.battleStatistic.addUser(lobbyUser.getUser(), lobbyUser.getSpaceshipSelected(), team);
        this.actorsManager.addSpaceship(spaceship);
    }

    private int randomLocation() {
        Random r = new Random();
        return r.nextInt(BattleUtils.MAP_SIZE.height - 80) + 30;
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
        if (dead.getTeam() == Constants.BLUE_TEAM) {
            dead.updateLocation(150, randomLocation());
            this.battleStatistic.incrementRedTeamPoint();
        } else {
            dead.updateLocation(650, randomLocation());
            this.battleStatistic.incrementBlueTeamPoint();
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
            PersonalStatistic personalStatistic = this.battleStatistic.getPersonalStatistic(userId);
            BattleData data = this.battleDatas.get(userId);
            data.setBlueTeamPoint(this.battleStatistic.getBlueTeamPoint());
            data.setRedTeamPoint(this.battleStatistic.getRedTeamPoint());
            data.setMatchTime(this.currentMatchTime);
            data.setKills(personalStatistic.getKills());
            data.setDeaths(personalStatistic.getDeaths());
            data.setCanUseShot(spaceship.canUseShot());
            data.setCanUseSkill(spaceship.canUseSkill());
            data.setMyHp(spaceship.getHP());
            return data;
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

    public void update(long time) {
        if(room.getState() == Constants.PLAYING){
            this.currentMatchTime = room.getMatchTime() - (System.currentTimeMillis() - this.startedAt);
            if (this.currentMatchTime > 0) {
                this.processActions();
                this.actorsManager.update(time);
            }else{
                room.endBattle();
            }
        }
    }
}
