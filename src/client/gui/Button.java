package client.gui;


public abstract class Button extends Component{

    private int width;
    private int height;
    private boolean pressed;
    private final ActionPerfomed action;

    public Button(int x, int y, ActionPerfomed action) {
        super(x, y);
        this.action = action;
    }

    protected final void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public final int getWidth(){
        return this.width;
    }
    
    public final int getHeight(){
        return this.height;
    }
    
    public final boolean isPressed(){
        return this.pressed;
    }
    
    private boolean checkIsPressed(double x, double y) {
        return x >= getX() && x <= getX() + width
                && y >= getY() && y <= getY() + height;
    }

    public final void mousePressed(double x, double y) {
        if (checkIsPressed(x, y)) {
            this.pressed = true;
        }
    }

    public final void mouseReleased() {
        if (pressed) {
            doClick();
            this.pressed = false;
        }
    }

    public final void doClick() {
        if (action != null) {
            action.doAction();
        }
    }
}
