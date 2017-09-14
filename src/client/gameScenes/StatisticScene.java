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
import client.gui.ImageButton;
import client.gui.ImageLabel;
import client.gui.StatisticPanel;
import client.input.Input;
import client.sprite.ExternalFileLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import server.room.battle.BattleStatistic;
import server.room.battle.PersonalStatistic;
import server.serverConstants.ServerConstants;

public final class StatisticScene extends GameScene {

    private BattleStatistic statistic;

    public StatisticScene(GameContext context, BattleStatistic statistic) {
        super(context, ExternalFileLoader.getInstance().getImage("client/images/background.png"));
        this.statistic = statistic;
        initComponents();
    }

    public void initComponents() {
        Image defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btExitRoom.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btExitRoom_onclick.png");
        Button btBack = new ImageButton(336, 545, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                changeScene(new RoomScene(getContext()));
            }
        });
        int x;
        int winner = statistic.getWinner();
        if (winner == ServerConstants.BLUE_TEAM) {
            x = 240;
            defaultImage = ExternalFileLoader.getInstance().getImage("client/images/blueWinsText.png");
        } else if (winner == ServerConstants.RED_TEAM) {
            x = 252;
            defaultImage = ExternalFileLoader.getInstance().getImage("client/images/redWinsText.png");
        } else {
            x = 350;
            defaultImage = ExternalFileLoader.getInstance().getImage("client/images/drawText.png");
        }
        ImageLabel textWinner = new ImageLabel(x, 30, defaultImage);
        StatisticPanel panel = new StatisticPanel(45, 80, statistic.getBlueTeam(), statistic.getRedTeam());
        addComponents(btBack, textWinner, panel);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
    }

    @Override
    public void update(Input input) {
        super.update(input);
    }
}
