package client.gui;

import client.sprite.Sprite;
import javafx.scene.canvas.GraphicsContext;

public class Animation extends Component {

    private Sprite sprite;
    private int translateX;
    private int translateY;
    public Animation(int x, int y, Sprite sprite) {
        super(x, y);
        this.sprite = sprite;
        this.translateX = 0;
        this.translateY = 0;
    }
    
    public Animation(int x, int y, Sprite sprite, int translateX, int translateY){
        this(x, y, sprite);
        this.translateX = translateX;
        this.translateY = translateY;        
    }
  
    public void setSpriteDirection(int direction){
        this.sprite.setDirection(direction);
    }
    
    public void render(GraphicsContext gc, int x, int y) {
        this.sprite.render(gc, x, y);
    }
    
    @Override
    public void render(GraphicsContext gc) {
        this.sprite.render(gc, getX() - translateX, getY() - translateY);
        this.sprite.update();
    }
   
}
