package client.gui;


public abstract class Button extends Component{

    private double width;
    private double height;
    private boolean pressed;
    private final ActionPerfomed action;

    public Button(double x, double y, ActionPerfomed action) {
        super(x, y);
        this.action = action;
        this.pressed = false;
    }

    protected final void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public final double getWidth(){
        return this.width;
    }
    
    public final double getHeight(){
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

    public void doClick() {
        if (action != null) {
            action.doAction();
        }
    }
}
