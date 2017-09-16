package client.sprite;

import javafx.scene.canvas.GraphicsContext;

public class Sprite {

    private static int SPRITE_COUNTER = 6; //velocidade com que a sprite troca de imagem
    private Spritesheet spritesheet;
    private int countToNextImage; //contador para mudar de imagem
    private int countCurrentlyImage; //imagem atual
    private int currenttlyDirection; //direção atual

    public Sprite(Spritesheet spritesheet) {
        this.spritesheet = spritesheet;
        this.countToNextImage = 0;
        this.countCurrentlyImage = 0;
        this.currenttlyDirection = 0;
    }

    public void setDirection(int direction) {
        this.currenttlyDirection = direction;
    }

    public void update() {
        this.countToNextImage++;
        if (this.countToNextImage == SPRITE_COUNTER) {
            this.countToNextImage = 0;
            this.countCurrentlyImage++;
        }
        if (this.countCurrentlyImage >= this.spritesheet.getTotalColumns()) {
            this.countCurrentlyImage = 0;
        }
    }

    public void render(GraphicsContext gc, double x, double y) {
        double sourceX = spritesheet.getWidthSubImage() * countCurrentlyImage;
        double sourceY = spritesheet.getHeightSubImage() * currenttlyDirection;
        gc.drawImage(spritesheet.getImage(), sourceX, sourceY,
                spritesheet.getWidthSubImage(), spritesheet.getHeightSubImage(), x, y,
                spritesheet.getWidthSubImage(), spritesheet.getHeightSubImage());
    }
}
