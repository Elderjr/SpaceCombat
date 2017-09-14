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
public class ImageLabel extends Component {

    private final Image image;

    public ImageLabel(int x, int y, Image image) {
        super(x, y);
        this.image = image;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isVisible()) {
            gc.drawImage(this.image, getX(), getY());
        }
    }

}
