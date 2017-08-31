/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import server.user.SimpleUser;

/**
 *
 * @author elderjr
 */
public class BattleStatistic implements Serializable{
    
    private HashMap<Long, PersonalStatistic> statistics;
    private int winner;
    
    public BattleStatistic(){
        this.statistics = new HashMap<>();
        this.winner = -1;
    }
    
    public void addUser(SimpleUser user, String spaceshipName){
        this.statistics.put(user.getId(), new PersonalStatistic(user, spaceshipName));
    }
    
    public Collection<PersonalStatistic> getPersonalStatistics(){
        return this.statistics.values();
    }
    
    public int getWinner(){
        return this.winner;
    }
    
    public void setWinner(int winner) {
        this.winner = winner;
    }
    
    public void incrementDeath(SimpleUser user) {
        this.statistics.get(user.getId()).incrementDeath();
    }

    public void incrementKills(SimpleUser user) {
        this.statistics.get(user.getId()).incrementDeath();
    }

    public void incrementDamageTaken(SimpleUser user, int damage) {
        this.statistics.get(user.getId()).incrementDamageTaken(damage);
    }

    public void incrementDamage(SimpleUser user, int damage) {
        this.statistics.get(user.getId()).incrementDamage(damage);
    }

    public void incrementHealTaken(SimpleUser user, int heal) {
        this.statistics.get(user.getId()).incrementHealTaken(heal);
    }

    public void incrementHeal(SimpleUser user, int heal) {
        this.statistics.get(user.getId()).incrementHeal(heal);
    }
    
}
