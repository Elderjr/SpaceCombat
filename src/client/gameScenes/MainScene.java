package client.gameScenes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import client.gui.*;
import client.sprite.ExternalFileLoader;
import client.windows.LoginForm;

public final class MainScene extends GameScene {

    private static final Font TITLE_FONT = Font.font("Times New Roman", FontWeight.BOLD, 48);
    public static final String CONNECTION_ERROR = "Erro na Conexao";
    public static final String NOTLOGGED_ERROR = "Login Expirado";
    private String msgError;

    public MainScene(GameContext context) {
        super(context, ExternalFileLoader.getInstance().getImage("client/images/main_screen.png"));
        this.msgError = null;
        initComponents();
    }

    public MainScene(GameContext context, String msgError) {
        this(context);
        this.msgError = msgError;
    }

    public void initComponents() {        
        Image defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btPlay.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btPlay_onclick.png");
        Button btGameStart = new ImageButton(295, 200, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                LoginForm loginForm = new LoginForm(null, true);
                loginForm.setVisible(true);
                loginForm.setFocusable(true);
                if(loginForm.getUser() != null){
                    changeScene(new RoomScene(getContext()));
                }
            }
        });
        defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btExitGame.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btExitGame_onclick.png");
        Button btExit = new ImageButton(295, 350, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                System.exit(0);
            }
        });
        addComponents(btGameStart, btExit);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(TITLE_FONT);
        gc.fillText("SPACE COMBAT", 210, 50);
        gc.strokeText("SPACE COMBAT", 210, 50);
        if (msgError != null) {
            gc.fillText(msgError, 210, 200);
            gc.strokeText(msgError, 210, 200);
        }
        this.renderComponents(gc);
    }
}
