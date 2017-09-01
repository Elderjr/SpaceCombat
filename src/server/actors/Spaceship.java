package server.actors;

import java.awt.Dimension;
import java.awt.Point;

import client.commands.ClientCommands;
import server.room.battle.BattleListener;
import server.serverConstants.ServerConstants;
import server.data.User;

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

    public int getHP() {
        return this.hp;
    }

    public void restoreHP() {
        this.hp = this.maxHP;
    }

    public void receiveDamage(Skill skill) {
        int damageTaken = 0;
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

    public Skill shoot() {
        if (canUseShot()) {
            Point shotLocation = new Point(this.getLocation().x, this.getLocation().y);
            if (getCurrentDirection() == ClientCommands.UP) {
                shotLocation.y -= this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == ClientCommands.DOWN) {
                shotLocation.y += this.getSize().getHeight() / 2;
            } else if (getCurrentDirection() == ClientCommands.LEFT) {
                shotLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.RIGHT) {
                shotLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.UP_LEFT) {
                shotLocation.y -= this.getSize().getHeight() / 2;
                shotLocation.x -= this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.UP_RIGHT) {
                shotLocation.y -= this.getSize().getHeight() / 2;
                shotLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.DOWN_RIGHT) {
                shotLocation.y += this.getSize().getHeight() / 2;
                shotLocation.x += this.getSize().getWidth() / 2;
            } else if (getCurrentDirection() == ClientCommands.DOWN_LEFT) {
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
        if (direction == ClientCommands.UP) {
            y -= movimentSpeed;
        } else if (direction == ClientCommands.UP_LEFT) {
            y -= movimentSpeed;
            x -= movimentSpeed;
        } else if (direction == ClientCommands.UP_RIGHT) {
            y -= movimentSpeed;
            x += movimentSpeed;
        } else if (direction == ClientCommands.DOWN) {
            y += movimentSpeed;
        } else if (direction == ClientCommands.DOWN_LEFT) {
            y += movimentSpeed;
            x -= movimentSpeed;
        } else if (direction == ClientCommands.DOWN_RIGHT) {
            y += movimentSpeed;
            x += movimentSpeed;
        } else if (direction == ClientCommands.LEFT) {
            x -= movimentSpeed;
        } else if (direction == ClientCommands.RIGHT) {
            x += movimentSpeed;
        }
        if (!(x > ServerConstants.MAP_WIDTH || x < 0 || y > ServerConstants.MAP_HEIGHT || y < 0)) {
            updateLocation(x, y);
        }
        setCurrentDirection(direction);
    }

    @Override
    public boolean update() {
        return false;
    }
}
