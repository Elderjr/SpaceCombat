/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author elderjr
 */
public class ImageButton extends Button{
    
    private final Image defaultImage;
    private final Image onClick;
    
    public ImageButton(int x, int y, Image defaultImage, Image onClick, ActionPerfomed action) {
        super(x, y, action);
        setSize((int) defaultImage.getWidth(), (int) defaultImage.getHeight());
        this.defaultImage = defaultImage;
        this.onClick = onClick;
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (isPressed()) {
            gc.drawImage(this.onClick, getX(), getY());
        } else {
            gc.drawImage(this.defaultImage, getX(), getY());
        }
    }
}
