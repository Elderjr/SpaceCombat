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
import client.network.ClientNetwork;
import client.input.ExternalFileLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import server.room.battle.BattleStatistic;
import constants.Constants;
import java.rmi.RemoteException;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.exceptions.NotLoggedException;

public final class StatisticScene extends GameScene {

    private static final Font PING_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 15);
    private BattleStatistic statistic;
    private long ping;
    private long lastPing;
    
    public StatisticScene(GameContext context, BattleStatistic statistic) {
        super(context, ExternalFileLoader.getInstance().getImage("background.png"));
        this.statistic = statistic;
        this.ping = 0;
        this.lastPing = 0;
        initComponents();
    }

    public void initComponents() {
        Image defaultImage = ExternalFileLoader.getInstance().getImage("btExitRoom.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("btExitRoom_onclick.png");
        Button btBack = new ImageButton(336, 545, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                changeScene(new RoomScene(getContext()));
            }
        });
        int x;
        int winner = statistic.getWinner();
        if (winner == Constants.BLUE_TEAM) {
            x = 240;
            defaultImage = ExternalFileLoader.getInstance().getImage("blueWinsText.png");
        } else if (winner == Constants.RED_TEAM) {
            x = 252;
            defaultImage = ExternalFileLoader.getInstance().getImage("redWinsText.png");
        } else {
            x = 350;
            defaultImage = ExternalFileLoader.getInstance().getImage("drawText.png");
        }
        ImageLabel textWinner = new ImageLabel(x, 30, defaultImage);
        StatisticPanel panel = new StatisticPanel(45, 80, statistic.getBlueTeam(), statistic.getRedTeam());
        addComponents(btBack, textWinner, panel);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
        gc.setFill(Color.YELLOW);
        gc.setFont(PING_FONT);
        gc.fillText("ping: "+this.ping+"ms", 20, 15);
    }

    @Override
    public void update(Input input) {
        try {
            super.update(input);
            if(System.currentTimeMillis() - this.lastPing >= 1000){
                this.ping = ClientNetwork.getInstance().ping();
                this.lastPing = System.currentTimeMillis();
            }
        } catch (RemoteException ex) {
            changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
        } catch (NotLoggedException ex){
            changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
        }
    }
}
