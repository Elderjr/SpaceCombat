/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.sprite.ExternalFileLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author elderjr
 */
public class HpBar extends Component {

    private static final Image BAR_IMAGE = ExternalFileLoader.getInstance().getImage("client/images/hpBar.png");
    private static final Font HP_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 15);
    private int maxHP;
    private int currentHP;
    private int currentPerc;

    public HpBar(int x, int y) {
        super(x, y);
    }

    private void calculePercentage() {
        if (this.maxHP != 0) {
            this.currentPerc = (int) ((this.currentHP / this.maxHP) * BAR_IMAGE.getWidth());
        } else {
            this.currentPerc = 0;
        }
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
        calculePercentage();
    }

    public void setCurrentHP(int hp) {
        this.currentHP = hp;
        calculePercentage();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (this.maxHP != 0) {
            gc.drawImage(BAR_IMAGE, getX(), getY(), currentPerc, BAR_IMAGE.getHeight());
        }
        gc.setFont(HP_FONT);
        gc.fillText(currentHP + " / " + maxHP, getX() + (BAR_IMAGE.getWidth() / 2) - 25, getY() + 13);
    }

}
