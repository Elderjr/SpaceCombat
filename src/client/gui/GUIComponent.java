package client.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class GUIComponent extends Component {

    private String name;
    private Font font;
    
    public GUIComponent(int x, int y) {
        super(x, y);
        this.font = Font.font("Times New Roman", FontWeight.NORMAL, 12);
    }

    public GUIComponent(int x, int y, String name) {
        super(x, y);
        this.name = name;
        this.font = Font.font("Times New Roman", FontWeight.NORMAL, 12);
    }

    @Override
    public void render(GraphicsContext gc){
        gc.setFont(this.font);
    }
    
    public String getName() {
        return this.name;
    }
}
