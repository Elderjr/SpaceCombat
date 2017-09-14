package client.gui;

public abstract class Component implements Paintable {

    private int x;
    private int y;
    private boolean visible;

    protected Component(int x, int y) {
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
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void updateLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
}
