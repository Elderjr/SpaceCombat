package client.gui;

import client.input.Input;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;

public class ComponentManager {

    private LinkedList<Component> components;
    private LinkedList<Button> buttons;

    public ComponentManager() {
        this.components = new LinkedList<>();
        this.buttons = new LinkedList<>();
    }

    public void addComponents(Component... comps) {
        for (Component p : comps) {
            addComponent(p);
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        if (c instanceof Button) {
            this.buttons.add((Button) c);
        }
    }

    public void processMouseInput(int state, double x, double y) {
        if (state == Input.MOUSE_PRESSED) {
            for (Button bt : buttons) {
                bt.mousePressed(x, y);
            }
        } else if (state == Input.MOUSE_RELEASED) {
            for (Button bt : buttons) {
                bt.mouseReleased();
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (Paintable p : components) {
            p.render(gc);
        }
    }
}
