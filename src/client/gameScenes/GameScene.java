package client.gameScenes;

import client.gui.Component;
import client.gui.ComponentManager;
import client.input.Input;
import client.windows.GameWindow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import constants.Constants;

public abstract class GameScene {

    private ComponentManager paintableManager;
    private GameContext gameContext;
    private Image background;

    public GameScene(GameContext context) {
        this.paintableManager = new ComponentManager();
        this.gameContext = context;
    }

    public GameScene(GameContext context, Image background) {
        this(context);
        this.background = background;
    }

    public void update(Input input) {
        this.paintableManager.processMouseInput(input.getMouseState(), input.getMouseX(), input.getMouseY());
    }

    public void changeScene(GameScene scene) {
        this.gameContext.changeScene(scene);
    }

    public void render(GraphicsContext gc) {
        if (this.background != null) {
            gc.drawImage(this.background, 0, 0);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
        }
    }

    protected GameContext getContext() {
        return this.gameContext;
    }

    protected void renderComponents(GraphicsContext gc) {
        this.paintableManager.render(gc);
    }

    protected void addComponent(Component component) {
        this.paintableManager.addComponent(component);
    }

    protected void addComponents(Component... components) {
        this.paintableManager.addComponents(components);
    }

}
