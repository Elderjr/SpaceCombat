package client.windows;

import client.gameScenes.GameContext;
import client.gameScenes.GameScene;
import client.gameScenes.LoadingScene;
import client.input.Input;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import server.ServerEngine;

public class GameWindow extends Application implements GameContext {

    private Canvas canvas;
    private GameScene gameScene;
    private Input input;

    public static void start() {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        launch();
    }

    public void start(Stage stage) {
        stage.setTitle("SPACE COMBAT");
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        this.input = new Input();
        initActionHandlers(scene);
        initGame(root);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
    }

    private void initActionHandlers(Scene scene) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                input.mousePressed(event.getX(), event.getY());
            }

        });
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                input.mouseReleased(event.getX(), event.getY());
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                input.keyPressed(event.getCode().getName());
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                input.keyReleased(event.getCode().getName());
            }
        });
    }

    private void initGame(Group root) {
        canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        this.gameScene = new LoadingScene(this);
        initGameLoop(canvas.getGraphicsContext2D());
    }

    private void initGameLoop(GraphicsContext gc) {
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017), // 60 FPS
                new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                gameScene.render(gc);
                gameScene.update(input);
                ServerEngine.getInstance().update();
            }
        });
        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    @Override
    public void changeScene(GameScene gameScene) {
        this.gameScene = gameScene;
    }
}
