package client.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Button extends GUIComponent {

    private int width;
    private int height;
    private boolean pressed;
    private ActionPerfomed action;

    public Button(int x, int y, String name, int width, int height, ActionPerfomed action) {
        super(x, y, name);
        this.width = width;
        this.height = height;
        this.action = action;
    }

    protected void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }

    public void render(GraphicsContext gc) {
        super.render(gc);
        if (pressed) {
            gc.setFill(Color.BLUE);
        } else {
            gc.setFill(Color.YELLOW);
        }
        gc.fillRect(getX(), getY(), width, height);
        if (getName() != null) {
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.RED);
            gc.fillText(getName(), getX() + 20, getY() + 20);
            gc.strokeText(getName(), getX() + 20, getY() + 20);
        }
        //int stringWidth = g.getFontMetrics().stringWidth(getName());
        //gc.drawString(getName(), getX() + width / 2 - stringWidth / 2, getY() + height / 2);
    }

    public boolean isPressed(){
        return this.pressed;
    }
    
    private boolean checkIsPressed(double x, double y) {
        return x >= getX() && x <= getX() + width
                && y >= getY() && y <= getY() + height;
    }

    public void mousePressed(double x, double y) {
        if (checkIsPressed(x, y)) {
            this.pressed = true;
        }
    }

    public void mouseReleased() {
        if (pressed) {
            doClick();
            this.pressed = false;
        }
    }

    public void doClick() {
        if (action != null) {
            action.doAction();
        }
    }
}
