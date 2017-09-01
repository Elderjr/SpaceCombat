/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gameScenes;

/**
 *
 * @author elderjr
 */
import client.gui.ActionPerfomed;
import client.gui.Button;
import client.input.Input;
import client.windows.GameContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import server.room.battle.BattleStatistic;
import server.room.battle.PersonalStatistic;
import server.serverConstants.ServerConstants;

public final class StatisticScene extends GameScene {

    private BattleStatistic statistic;

    public StatisticScene(GameContext context, BattleStatistic statistic) {
        super(context);
        initComponents();
        this.statistic = statistic;
    }

    public void initComponents() {
        Button btBack = new Button(600, 500, "back", 60, 30, new ActionPerfomed() {
            @Override
            public void doAction() {
                changeScene(new RoomScene(getContext()));
            }
        });
        addComponents(btBack);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
        if(statistic.getWinner() == ServerConstants.BLUE_TEAM){
            gc.setFill(Color.BLUE);
            gc.fillText("BLUE WINNER", 10, 10);
        }else if(statistic.getWinner() == ServerConstants.RED_TEAM){
            gc.setFill(Color.RED);
            gc.fillText("RED WINNER", 10, 10);
        }else if(statistic.getWinner() == ServerConstants.DRAW){
            gc.setFill(Color.WHITE);
            gc.fillText("DRAW", 10, 10);
        }
        gc.setFill(Color.GREEN);
        gc.fillText("User", 30, 30);
        gc.fillText("Kills", 230, 30);
        gc.fillText("Deaths", 330, 30);
        gc.fillText("Damage", 430, 30);
        gc.fillText("Damage Taken", 530, 30);
        gc.fillText("Heal", 630, 30);
        gc.fillText("Heal Taken", 730, 30);
        int y = 50;
        gc.setFill(Color.BLUE);
        for (PersonalStatistic statistic : statistic.getBlueTeam()) {
            gc.fillText("" + statistic.getUser().getUsername() + " (" + statistic.getSpaceshipName() + ")", 30, 30 + y);
            gc.fillText("" + statistic.getKills(), 230, 30 + y);
            gc.fillText("" + statistic.getDeaths(), 330, 30 + y);
            gc.fillText("" + statistic.getDamage(), 430, 30 + y);
            gc.fillText("" + statistic.getDamageTaken(), 530, 30 + y);
            gc.fillText("" + statistic.getHeal(), 630, 30 + y);
            gc.fillText("" + statistic.getHealTaken(), 730, 30 + y);
            y += 20;
        }
        gc.setFill(Color.RED);
        for (PersonalStatistic statistic : statistic.getRedTeam()) {
            gc.fillText("" + statistic.getUser().getUsername() + " (" + statistic.getSpaceshipName() + ")", 30, 30 + y);
            gc.fillText("" + statistic.getKills(), 230, 30 + y);
            gc.fillText("" + statistic.getDeaths(), 330, 30 + y);
            gc.fillText("" + statistic.getDamage(), 430, 30 + y);
            gc.fillText("" + statistic.getDamageTaken(), 530, 30 + y);
            gc.fillText("" + statistic.getHeal(), 630, 30 + y);
            gc.fillText("" + statistic.getHealTaken(), 730, 30 + y);
            y += 20;
        }
    }

    @Override
    public void update(Input input) {
        super.update(input);
    }
}
