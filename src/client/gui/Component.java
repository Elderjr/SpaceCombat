package client.gui;

public abstract class Component implements Paintable {

    private int x;
    private int y;

    protected Component(int x, int y) {
        this.x = x;
        this.y = y;
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
