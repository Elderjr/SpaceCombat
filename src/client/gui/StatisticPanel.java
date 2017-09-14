/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.sprite.ExternalFileLoader;
import java.util.Collection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import server.room.battle.PersonalStatistic;

/**
 *
 * @author elderjr
 */
public class StatisticPanel extends Component{

    private static final Image STATISTIC_LINE = ExternalFileLoader.getInstance().getImage("client/images/statisticLine.png");
    private static final Image BLUE_LINE = ExternalFileLoader.getInstance().getImage("client/images/blueLine.png");
    private static final Image RED_LINE = ExternalFileLoader.getInstance().getImage("client/images/redLine.png");
    
    private Collection<PersonalStatistic> blueTeam;
    private Collection<PersonalStatistic> redTeam;
    
    public StatisticPanel(int x, int y, Collection<PersonalStatistic> blueTeam, Collection<PersonalStatistic> redTeam) {
        super(x, y);
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
    }
    

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(STATISTIC_LINE, getX(), getY());
        int y = getY() + 30;
        gc.setFill(Color.BLUE);
        for (PersonalStatistic statistic : this.blueTeam) {
            gc.drawImage(BLUE_LINE, getX() + 6, y);
            gc.fillText("" + statistic.getUser().getUsername() + " (" + statistic.getSpaceshipName() + ")", getX() + 15, y + 20);
            gc.fillText("" + statistic.getKills(), getX() + 175, y + 20);
            gc.fillText("" + statistic.getDeaths(), getX() + 235, y + 20);
            gc.fillText("" + statistic.getDamage(), getX() + 345, y + 20);
            gc.fillText("" + statistic.getDamageTaken(), getX() + 410, y + 20);
            gc.fillText("" + statistic.getHeal(), 608, y + 20);
            gc.fillText("" + statistic.getHealTaken(), 660, y + 20);
            y += 40;
        }
        gc.setFill(Color.RED);
        for (PersonalStatistic statistic : this.redTeam) {
            gc.drawImage(RED_LINE, getX() + 6, y);
            gc.fillText("" + statistic.getUser().getUsername() + " (" + statistic.getSpaceshipName() + ")", getX() + 15, y + 20);
            gc.fillText("" + statistic.getKills(), getX() + 175, y + 20);
            gc.fillText("" + statistic.getDeaths(), getX() + 235, y + 20);
            gc.fillText("" + statistic.getDamage(), getX() + 345, y + 20);
            gc.fillText("" + statistic.getDamageTaken(), getX() + 410, y + 20);
            gc.fillText("" + statistic.getHeal(), 608, y + 20);
            gc.fillText("" + statistic.getHealTaken(), 660, y + 20);
            y += 40;
        }
    }
    
}
