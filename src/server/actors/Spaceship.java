package server.actors;

import constants.Constants;
import java.awt.Dimension;
import java.awt.Point;

import server.room.battle.BattleListener;
import server.data.User;
import server.room.battle.BattleUtils;

public abstract class Spaceship extends Actor implements Moviment {

    private static Dimension SIZE = new Dimension(64, 64);    
    protected long shotFired; //armazena quando o usuário atirou
    protected long skillFired; //armazena quando o usuário usou skill

    private final User pilot;
    private final int maxHP;
    private final int movimentSpeed;
    private final int team;
    private int hp;

    public Spaceship(BattleListener room, Point location, int team, String actorType, int maxHP, int movimentSpeed, int initialDirection, User pilot) {
        super(room, location, SIZE, team, actorType, initialDirection);
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.team = team;
        this.shotFired = 0;
        this.skillFired = 0;
        this.movimentSpeed = movimentSpeed;
        this.pilot = pilot;
    }

    public abstract Skill useSkill();

    public abstract boolean canUseSkill();

    public User getPilot() {
        return this.pilot;
    }

    public int getMaxHP(){
        return this.maxHP;
    }
    
    public int getHP() {
        return this.hp;
    }

    public void restoreHP() {
        this.hp = this.maxHP;
    }

    public void receiveDamage(Skill skill) {
        int damageTaken;
        if (this.hp <= skill.getDamage()) {
            damageTaken = this.hp;
            this.hp = 0;
            getBattleListener().deathNotification(this, skill.getSource());
        } else {
            damageTaken = skill.getDamage();
            this.hp -= skill.getDamage();
        }
        getBattleListener().damageNotification(this, skill.getSource(), damageTaken);
    }

    public void receiveHeal(Skill skill) {
        int healTaken = 0;
        if (this.hp + skill.getDamage() > this.maxHP) {
            healTaken = this.maxHP - this.hp;
            this.hp = this.maxHP;
        } else {
            healTaken = skill.getDamage();
            this.hp += skill.getDamage();
        }
        getBattleListener().healNotification(this, skill.getSource(), healTaken);
    }

    public boolean canUseShot() {
        return System.currentTimeMillis() - shotFired >= Shot.COOLDOWN;
    }

    public Shot shoot() {
        if (canUseShot()) {
            Point shotLocation = new Point(this.getLocation().x, this.getLocation().y);
            if (getCurrentDirection() == Constants.UP) {
                shotLocation.y -= this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.DOWN) {
                shotLocation.y += this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.LEFT) {
                shotLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.RIGHT) {
                shotLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_LEFT) {
                shotLocation.y -= this.getSize().getHeight() / 2;
                shotLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_RIGHT) {
                shotLocation.y -= this.getSize().getHeight() / 2;
                shotLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_RIGHT) {
                shotLocation.y += this.getSize().getHeight() / 2;
                shotLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_LEFT) {
                shotLocation.y += this.getSize().getHeight() / 2;
                shotLocation.x -= this.getSize().getWidth() / 2;
            }
            this.shotFired = System.currentTimeMillis();
            return new Shot(getBattleListener(), shotLocation, this, getCurrentDirection());
        }else{
            return null;
        }
    }

    @Override
    public void move(int direction) {
        int x = this.getLocation().x;
        int y = this.getLocation().y;
        if (direction == Constants.UP) {
            y -= movimentSpeed;
        } else if (direction == Constants.UP_LEFT) {
            y -= movimentSpeed;
            x -= movimentSpeed;
        } else if (direction == Constants.UP_RIGHT) {
            y -= movimentSpeed;
            x += movimentSpeed;
        } else if (direction == Constants.DOWN) {
            y += movimentSpeed;
        } else if (direction == Constants.DOWN_LEFT) {
            y += movimentSpeed;
            x -= movimentSpeed;
        } else if (direction == Constants.DOWN_RIGHT) {
            y += movimentSpeed;
            x += movimentSpeed;
        } else if (direction == Constants.LEFT) {
            x -= movimentSpeed;
        } else if (direction == Constants.RIGHT) {
            x += movimentSpeed;
        }
        if (!BattleUtils.isOutside(x, y)) {
            updateLocation(x, y);
        }
        setCurrentDirection(direction);
    }

    @Override
    public boolean update() {
        return false;
    }
}
