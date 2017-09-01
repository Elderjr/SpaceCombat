/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import server.actors.Spaceship;

/**
 *
 * @author elderjr
 */
public class BattleUser {
    
    private final Spaceship spaceship;
    private int directionToMove;
    private boolean useSkill;
    private boolean shoot;

    public BattleUser(Spaceship spaceship) {
        this.spaceship = spaceship;
        this.directionToMove = -1;
        this.useSkill = false;
        this.shoot = false;
    }

    public Spaceship getSpaceship(){
        return this.spaceship;
    }
    
    public int getDirectionToMove() {
        return directionToMove;
    }

    public void setDirectionToMove(int directionToMove) {
        this.directionToMove = directionToMove;
    }

    public boolean isUseSkill() {
        return useSkill;
    }

    public void setUseSkill(boolean useSkill) {
        this.useSkill = useSkill;
    }

    public boolean isShoot() {
        return shoot;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }
}
