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
    private final double movimentSpeed;
    private final int team;
    private int hp;

    public Spaceship(BattleListener room, Position location, int team, String actorType, int maxHP, double movimentSpeed, int initialDirection, User pilot) {
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
    
    public double getMovimentSpeed(){
        return this.movimentSpeed;
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
            double x = this.getLocation().getX();
            double y = this.getLocation().getY();
            if (getCurrentDirection() == Constants.UP) {
                y -= this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.DOWN) {
                y += this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == Constants.LEFT) {
                x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.RIGHT) {
                x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_LEFT) {
                y -= this.getSize().getHeight() / 2;
                x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.UP_RIGHT) {
                y -= this.getSize().getHeight() / 2;
                x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_RIGHT) {
                y += this.getSize().getHeight() / 2;
                x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == Constants.DOWN_LEFT) {
                y += this.getSize().getHeight() / 2;
                x -= this.getSize().getWidth() / 2;
            }
            this.shotFired = System.currentTimeMillis();
            return new Shot(getBattleListener(), new Position(x, y), this, getCurrentDirection());
        }else{
            return null;
        }
    }

    
    public void move(int direction){
        move(direction, this.movimentSpeed);
    }
    @Override
    public void move(int direction, double speed) {
        double x = this.getLocation().getX();
        double y = this.getLocation().getY();
        if (direction == Constants.UP) {
            y -= speed;
        } else if (direction == Constants.UP_LEFT) {
            y -= speed;
            x -= speed;
        } else if (direction == Constants.UP_RIGHT) {
            y -= speed;
            x += speed;
        } else if (direction == Constants.DOWN) {
            y += speed;
        } else if (direction == Constants.DOWN_LEFT) {
            y += speed;
            x -= speed;
        } else if (direction == Constants.DOWN_RIGHT) {
            y += speed;
            x += speed;
        } else if (direction == Constants.LEFT) {
            x -= speed;
        } else if (direction == Constants.RIGHT) {
            x += speed;
        }
        if (!BattleUtils.isOutside(x, y)) {
            updateLocation(x, y);
        }
        setCurrentDirection(direction);
    }

    @Override
    public boolean update(long time) {
        return false;
    }
}
