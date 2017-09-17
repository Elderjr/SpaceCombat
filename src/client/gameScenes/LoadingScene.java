package client.gameScenes;

import client.ClientResource;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import client.input.Input;
import client.input.ExternalFileLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import constants.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipInputStream;
import javafx.scene.image.Image;

public final class LoadingScene extends GameScene {

    private static final int EXTERNAL_FILES = 50;
    private static final Font LOADING_FONT = Font.font("Times New Roman", FontWeight.BOLD, 48);
    private Thread loadThread;
    private int totalFiledLoaded;
    private int currentPercentage;
    private boolean done;

    public LoadingScene(GameContext context) {
        super(context);
        this.totalFiledLoaded = 0;
        this.currentPercentage = 0;
        this.done = false;
        initThread();
    }

    public void initThread() {
        this.loadThread = new Thread(new Runnable() {
            private void loadSprite(String spritesheet) {
                ExternalFileLoader.getInstance().getSprite(spritesheet);
                totalFiledLoaded++;
                calculeCurrentPercentage();
            }

            private void loadImage(String image) {
                ExternalFileLoader.getInstance().getImage(image);
                totalFiledLoaded++;
                calculeCurrentPercentage();
            }

            @Override
            public void run() {
                try (InputStream in = ClientResource.class.getResourceAsStream("images");
                        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    String resource;
                    while ((resource = br.readLine()) != null) {
                        if(resource.endsWith(".png")){
                            System.out.println("Loading image: "+resource);
                            loadImage(resource);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoadingScene.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try (InputStream in = ClientResource.class.getResourceAsStream("images/spritesheets");
                        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    String resource;
                    while ((resource = br.readLine()) != null) {
                        if(resource.endsWith(".png")){
                            String spritesheetName = resource.replace(".png", "");
                            System.out.println("Loading spritesheet: "+spritesheetName);
                            loadSprite(spritesheetName);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoadingScene.class.getName()).log(Level.SEVERE, null, ex);
                }
                done = true;
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
        if (done) {
            this.loadThread.stop();
            changeScene(new MainScene(getContext()));
        }
    }
}
