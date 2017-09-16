package client.gui;

public abstract class Component implements Paintable {

    private double x;
    private double y;
    private boolean visible;

    protected Component(double x, double y) {
        this.x = x;
        this.y = y;
        this.visible = true;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }
    
    public boolean isVisible(){
        return this.visible;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updateLocation(double x, double y){
        this.x = x;
        this.y = y;
    }
}
