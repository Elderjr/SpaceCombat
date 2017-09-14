/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import java.io.Serializable;
import server.room.battle.PersonalStatistic;

/**
 *
 * @author elderjr
 */
public class GeneralStatistics implements Serializable {

    private int matches;
    private int wins;
    private int loses;
    private int draws;
    private int kills;
    private int deaths;

    public GeneralStatistics(int matches, int wins, int draws, int loses, int kills, int deaths) {
        this.matches = matches;
        this.wins = wins;
        this.draws = draws;
        this.loses = loses;
        this.kills = kills;
        this.deaths = deaths;
    }

    public void incrementValues(PersonalStatistic statistic, boolean wins, boolean draw) {
        if (wins) {
            this.wins++;
        } else if (draw) {
            this.draws++;
        } else {
            this.loses++;
        }
        this.matches++;
        this.kills += statistic.getKills();
        this.deaths += statistic.getDeaths();
    }

    @Override
    public String toString() {
        return "Wins: " + this.wins + "   "
                + "Draws: " + this.draws + "   "
                + "Loses: " + this.loses + "   "
                + "Kills: " + this.kills + "   "
                + "Deaths: " + this.deaths;
    }

    /**
     * @return the matches
     */
    public int getMatches() {
        return matches;
    }

    /**
     * @return the wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * @return the loses
     */
    public int getLoses() {
        return loses;
    }

    /**
     * @return the kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * @return the deaths
     */
    public int getDeaths() {
        return deaths;
    }

}
