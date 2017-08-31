/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.room.battle;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import server.serverConstants.ServerConstants;
import server.user.User;

/**
 *
 * @author elderjr
 */
public class BattleStatistic implements Serializable{
    
    private HashMap<Long, PersonalStatistic> blueTeam;
    private HashMap<Long, PersonalStatistic> redTeam;
    private int winner;
    
    public BattleStatistic(){
        this.blueTeam = new HashMap<>();
        this.redTeam = new HashMap<>();
        this.winner = -1;
    }
    
    public void addUser(User user, String spaceshipName, int team){
        if(team == ServerConstants.BLUE_TEAM){
            this.blueTeam.put(user.getId(), new PersonalStatistic(user, spaceshipName));
        }else if(team == ServerConstants.RED_TEAM){
            this.redTeam.put(user.getId(), new PersonalStatistic(user, spaceshipName));
        }
    }
    
    public Collection<PersonalStatistic> getBlueTeam(){
        return this.blueTeam.values();
    }
    
    public Collection<PersonalStatistic> getRedTeam(){
        return this.redTeam.values();
    }
    
    public int getWinner(){
        return this.winner;
    }
    
    public void setWinner(int winner) {
        this.winner = winner;
    }
    
    public void incrementDeath(User user) {
        if(this.blueTeam.containsKey(user.getId())){
            this.blueTeam.get(user.getId()).incrementDeath();
        }else if(this.redTeam.containsKey(user.getId())){
            this.redTeam.get(user.getId()).incrementDeath();
        }
    }

    public void incrementKills(User user) {
        if(this.blueTeam.containsKey(user.getId())){
            this.blueTeam.get(user.getId()).incrementKills();
        }else if(this.redTeam.containsKey(user.getId())){
            this.redTeam.get(user.getId()).incrementKills();
        }
    }

    public void incrementDamageTaken(User user, int damage) {
        if(this.blueTeam.containsKey(user.getId())){
            this.blueTeam.get(user.getId()).incrementDamageTaken(damage);
        }else if(this.redTeam.containsKey(user.getId())){
            this.redTeam.get(user.getId()).incrementDamageTaken(damage);
        }
    }

    public void incrementDamage(User user, int damage) {
        if(this.blueTeam.containsKey(user.getId())){
            this.blueTeam.get(user.getId()).incrementDamage(damage);
        }else if(this.redTeam.containsKey(user.getId())){
            this.redTeam.get(user.getId()).incrementDamage(damage);
        }
    }

    public void incrementHealTaken(User user, int heal) {
        if(this.blueTeam.containsKey(user.getId())){
            this.blueTeam.get(user.getId()).incrementHealTaken(heal);
        }else if(this.redTeam.containsKey(user.getId())){
            this.redTeam.get(user.getId()).incrementHealTaken(heal);
        }
    }

    public void incrementHeal(User user, int heal) {
        if(this.blueTeam.containsKey(user.getId())){
            this.blueTeam.get(user.getId()).incrementHeal(heal);
        }else if(this.redTeam.containsKey(user.getId())){
            this.redTeam.get(user.getId()).incrementHeal(heal);
        }
    }
    
}
