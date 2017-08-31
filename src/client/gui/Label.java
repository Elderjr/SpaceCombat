package client.gui;

import javafx.scene.canvas.GraphicsContext;

public class Label extends GUIComponent {

    public Label(int x, int y, String name) {
        super(x, y, name);
    }


    @Override
    public void render(GraphicsContext gc) {
        if (getName() != null) {
            gc.fillText(getName(), getX(), getY());
        }
    }
}
