package client.gui;

import client.sprite.Sprite;
import javafx.scene.canvas.GraphicsContext;

public class Animation extends Component {

    private Sprite sprite;
    
    public Animation(double x, double y, Sprite sprite) {
        super(x, y);
        this.sprite = sprite;
    }
    
    public Animation(double x, double y, Sprite sprite, double translateX, double translateY) {
        super(x, y);
        this.sprite = sprite;
    }
  
    public void setSpriteDirection(int direction){
        this.sprite.setDirection(direction);
    }
    
    @Override
    public void render(GraphicsContext gc) {
        this.sprite.render(gc, getX(), getY());
        this.sprite.update();
    }
   
}
