package client.gameScenes;

import client.gui.Component;
import client.gui.ComponentManager;
import client.input.Input;
import client.windows.GameContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import server.serverConstants.ServerConstants;

public abstract class GameScene {

    private ComponentManager paintableManager;
    private GameContext gameContext;

    public GameScene(GameContext context) {
        this.paintableManager = new ComponentManager();
        this.gameContext = context;
    }

    public void update(Input input){
        this.paintableManager.processMouseInput(input.getMouseState(), input.getMouseX(), input.getMouseY());
    }

    public void changeScene(GameScene scene) {
        this.gameContext.changeScene(scene);
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, ServerConstants.MAP_WIDTH, ServerConstants.MAP_HEIGHT);
    }

    protected GameContext getContext(){
        return this.gameContext;
    }
    
    protected void renderComponents(GraphicsContext gc){
        this.paintableManager.render(gc);
    }
    
    protected void addComponent(Component component) {
        this.paintableManager.addComponent(component);
    }

    protected void addComponents(Component... components) {
        this.paintableManager.addComponents(components);
    }

}
