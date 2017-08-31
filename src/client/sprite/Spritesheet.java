package client.sprite;

import javafx.scene.image.Image;

public class Spritesheet {
  
    private Image image;
    private int totalLines;
    private int totalColumns;
    private double widthSubImage;
    private double heightSubImage;

    public Spritesheet(Image image, int totalLines, int totalColumns) {
        this.image = image;
        this.totalLines = totalLines;
        this.totalColumns = totalColumns;
        this.widthSubImage = image.getWidth() / totalColumns;
        this.heightSubImage = image.getHeight() / totalLines;
    }

    public int getTotalLines() {
        return this.totalLines;
    }

    public int getTotalColumns() {
        return this.totalColumns;
    }
    
    public double getWidthSubImage(){
        return this.widthSubImage;
    }
    
    public double getHeightSubImage(){
        return this.heightSubImage;
    }

    public Image getImage() {
        return this.image;
    }
}
