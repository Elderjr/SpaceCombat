package server.data;

import java.io.Serializable;
import java.util.HashMap;


public class BattleData implements Serializable {

    private HashMap<Long, SimpleActor> actors;
    private int blueTeamPoint;
    private int redTeamPoint;
    private boolean canUseShot;
    private boolean canUseSkill;
    private long matchTime;
    private int myHp;
    private int maxHp;
    private int kills;
    private int deaths;
    
    

    public BattleData(HashMap<Long, SimpleActor> actors, long matchTime, int maxHp) {
        this.actors = actors;
        this.matchTime = matchTime;
        this.canUseShot = true;
        this.canUseSkill = true;
        this.blueTeamPoint = 0;
        this.redTeamPoint = 0;
        this.myHp = maxHp;
        this.maxHp = maxHp;
    }

    /**
     * @return the actors
     */
    public HashMap<Long, SimpleActor> getActors() {
        return actors;
    }

    /**
     * @return the blueTeamPoint
     */
    public int getBlueTeamPoint() {
        return blueTeamPoint;
    }

    /**
     * @param blueTeamPoint the blueTeamPoint to set
     */
    public void setBlueTeamPoint(int blueTeamPoint) {
        this.blueTeamPoint = blueTeamPoint;
    }

    /**
     * @return the redTeamPoint
     */
    public int getRedTeamPoint() {
        return redTeamPoint;
    }

    /**
     * @param redTeamPoint the redTeamPoint to set
     */
    public void setRedTeamPoint(int redTeamPoint) {
        this.redTeamPoint = redTeamPoint;
    }

    /**
     * @return the canUseShot
     */
    public boolean canUseShot() {
        return canUseShot;
    }

    /**
     * @param canUseShot the canUseShot to set
     */
    public void setCanUseShot(boolean canUseShot) {
        this.canUseShot = canUseShot;
    }

    /**
     * @return the canUseSkill
     */
    public boolean canUseSkill() {
        return canUseSkill;
    }

    /**
     * @param canUseSkill the canUseSkill to set
     */
    public void setCanUseSkill(boolean canUseSkill) {
        this.canUseSkill = canUseSkill;
    }

    /**
     * @return the matchTime
     */
    public long getMatchTime() {
        return matchTime;
    }

    /**
     * @param matchTime the matchTime to set
     */
    public void setMatchTime(long matchTime) {
        this.matchTime = matchTime;
    }

    /**
     * @return the myHp
     */
    public int getMyHp() {
        return myHp;
    }

    /**
     * @param myHp the myHp to set
     */
    public void setMyHp(int myHp) {
        this.myHp = myHp;
    }
    
    public int getKills(){
        return this.kills;
    }
    
    public void setKills(int kills){
        this.kills = kills;
    }
    
    public int getDeaths(){
        return this.deaths;
    }
    
    public void setDeaths(int deaths){
        this.deaths = deaths;
    }
    public int getMaxHp(){
        return this.maxHp;
    }

}
