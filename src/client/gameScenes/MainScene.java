package client.gameScenes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import client.gui.*;
import client.network.ClientNetwork;
import client.windows.LoginForm;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

public final class MainScene extends GameScene {

    public static final String CONNECTION_ERROR = "Erro na Conexao";
    public static final String NOTLOGGED_ERROR = "Login Expirado";

    private Image background;
    private Font titleFont;
    private Button btGameStart;
    private String msgError;

    public MainScene(GameContext context) {
        super(context);
        this.msgError = null;
        initComponents();
    }

    public MainScene(GameContext context, String msgError) {
        this(context);
        this.msgError = msgError;
    }

    public void initComponents() {
        this.background = new Image("client/images/main_screen.png");
        this.titleFont = Font.font("Times New Roman", FontWeight.BOLD, 48);
        this.btGameStart = new Button(100, 100, "Game Start", 100, 100, new ActionPerfomed() {
            @Override
            public void doAction() {
                LoginForm loginForm = new LoginForm(null, true);
                loginForm.setVisible(true);
                if(loginForm.getUser() != null){
                    changeScene(new RoomScene(getContext()));
                }
            }
        });
        addComponent(btGameStart);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        gc.drawImage(this.background, 0, 0);
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(this.titleFont);
        gc.fillText("SPACE COMBAT", 210, 50);
        gc.strokeText("SPACE COMBAT", 210, 50);
        if (msgError != null) {
            gc.fillText(msgError, 210, 200);
            gc.strokeText(msgError, 210, 200);
        }
        this.renderComponents(gc);
    }
}
