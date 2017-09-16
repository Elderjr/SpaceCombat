package client.gameScenes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import client.input.Input;
import client.sprite.ExternalFileLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import constants.Constants;

public final class LoadingScene extends GameScene {

    private static final int EXTERNAL_FILES = 14;
    private static final Font LOADING_FONT = Font.font("Times New Roman", FontWeight.BOLD, 48);
    private Thread loadThread;
    private int totalFiledLoaded;
    private int currentPercentage;

    public LoadingScene(GameContext context) {
        super(context);
        this.totalFiledLoaded = 0;
        this.currentPercentage = 0;
        initThread();
    }

    public void initThread() {
        this.loadThread = new Thread(new Runnable() {
            private void loadSprite(String type, int team) {
                ExternalFileLoader.getInstance().getSprite(type, team);
                totalFiledLoaded++;
                calculeCurrentPercentage();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoadingScene.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void loadImage(String imageName) {
                ExternalFileLoader.getInstance().getImage(imageName);
                totalFiledLoaded++;
                calculeCurrentPercentage();
            }

            @Override
            public void run() {
                loadSprite(Constants.ASSAULTER_SKILL, Constants.BLUE_TEAM);
                loadSprite(Constants.ASSAULTER_SKILL, Constants.RED_TEAM);
                loadSprite(Constants.RAPTOR_SKILL, Constants.BLUE_TEAM);
                loadSprite(Constants.RAPTOR_SKILL, Constants.RED_TEAM);
                loadSprite(Constants.SUPPORTER_SKILL, Constants.BLUE_TEAM);
                loadSprite(Constants.SUPPORTER_SKILL, Constants.RED_TEAM);
                loadSprite(Constants.SHOT, Constants.BLUE_TEAM);
                loadSprite(Constants.SHOT, Constants.RED_TEAM);
                loadSprite(Constants.SPACESHIP_ASSAULTER, Constants.BLUE_TEAM);
                loadSprite(Constants.SPACESHIP_ASSAULTER, Constants.RED_TEAM);
                loadSprite(Constants.SPACESHIP_RAPTOR, Constants.BLUE_TEAM);
                loadSprite(Constants.SPACESHIP_RAPTOR, Constants.RED_TEAM);
                loadSprite(Constants.SPACESHIP_SUPPORTER, Constants.BLUE_TEAM);
                loadSprite(Constants.SPACESHIP_SUPPORTER, Constants.RED_TEAM);
            }
        });
        this.loadThread.start();
    }

    private void calculeCurrentPercentage() {
        this.currentPercentage = (int) (((double) this.totalFiledLoaded / (double) EXTERNAL_FILES) * 100);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        gc.setFill(Color.RED);
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);
        gc.setFont(LOADING_FONT);
        gc.strokeText("Loading", 0, 530);
        gc.fillText("Loading", 0, 530);
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 560, 8 * currentPercentage, 8);
    }

    @Override
    public void update(Input input) {
        super.update(input);
        if (totalFiledLoaded == EXTERNAL_FILES) {
            this.loadThread.stop();
            changeScene(new MainScene(getContext()));
        }
    }
}
