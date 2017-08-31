package server.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import server.actors.SimpleActor;

public class BattleData implements Serializable {

    private HashMap<Long, SimpleActor> actors;
    private int blueTeamPoint;
    private int redTeamPoint;
    private boolean canUseShot;
    private boolean canUseSkill;
    private long matchTime;
    private int myHp;

    public BattleData(HashMap<Long, SimpleActor> actors, int blueTeamPoint, int redTeamPoint, long matchTime, boolean canUseShot, boolean canUseSkill, int myHp) {
        this.actors = actors;
        this.canUseShot = canUseShot;
        this.canUseSkill = canUseSkill;
        this.blueTeamPoint = blueTeamPoint;
        this.redTeamPoint = redTeamPoint;
        this.matchTime = matchTime;
        this.myHp = myHp;
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

}
