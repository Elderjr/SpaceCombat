/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import java.io.Serializable;
import server.user.User;

/**
 *
 * @author elderjr
 */
public class PersonalStatistic implements Serializable {

    private User user;
    private String spaceshipName;
    private int deaths;
    private int kills;
    private int damageTaken;
    private int damage;
    private int healTaken;
    private int heal;

    public PersonalStatistic(User user, String spaceshipName) {
        this.user = user;
        this.spaceshipName = spaceshipName;
        this.deaths = 0;
        this.kills = 0;
        this.damageTaken = 0;
        this.damage = 0;
        this.healTaken = 0;
        this.heal = 0;
    }

    public User getSimpleUser(){
        return this.user;
    }
    
    public String getSpaceshipName(){
        return this.spaceshipName;
    }
    
    public void incrementDeath() {
        this.deaths++;
    }

    public void incrementKills() {
        this.kills++;
    }

    public void incrementDamageTaken(int damage) {
        this.damageTaken += damage;
    }

    public void incrementDamage(int damage) {
        this.damage += damage;
    }

    public void incrementHealTaken(int heal) {
        this.healTaken += heal;
    }

    public void incrementHeal(int heal) {
        this.heal += heal;
    }

    /**
     * @return the deaths
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * @return the kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * @return the damageTaken
     */
    public int getDamageTaken() {
        return damageTaken;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return the healTaken
     */
    public int getHealTaken() {
        return healTaken;
    }

    /**
     * @return the heal
     */
    public int getHeal() {
        return heal;
    }
}
