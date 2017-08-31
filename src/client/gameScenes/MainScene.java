package client.gameScenes;





import client.windows.GameContext;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import client.gui.*;
import client.network.ClientNetwork;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

public final class MainScene extends GameScene {

    private Image background;
    private Font titleFont;
    private Button btGameStart;
    private boolean error;
    
    public MainScene(GameContext context) {
        super(context);
        initComponents();
    }
    
    public MainScene(GameContext context, boolean error){
        this(context);
        this.error = error;
    }

    public void initComponents(){
        this.background = new Image("client/images/main_screen.png" );
        this.titleFont = Font.font("Times New Roman", FontWeight.BOLD, 48);
        this.btGameStart = new Button(100, 100, "Game Start", 100, 100, new ActionPerfomed() {
            @Override
            public void doAction() {          
                try{
                    boolean connectionSuccess = ClientNetwork.getInstance().connect("127.0.0.1");
                    if(connectionSuccess){
                        String username = JOptionPane.showInputDialog("Username:");
                        ClientNetwork.getInstance().login(username, "123");    
                        changeScene(new RoomScene(getContext()));    
                    }else{
                        System.out.println("Connection error");
                        error = true;
                    }
                } catch (RemoteException ex) {
                    error = true;
                }
            }
        });
        addComponent(btGameStart);
    }
    
    @Override
    public void render(GraphicsContext gc){
        super.render(gc);
        gc.drawImage(this.background, 0, 0);
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(this.titleFont);
        gc.fillText("SPACE COMBAT", 210, 50);
        gc.strokeText("SPACE COMBAT", 210, 50);
        if(error){
            gc.fillText("Erro na conexao", 210, 200);
            gc.strokeText("Erro na conexao", 210, 200);
        }
        this.renderComponents(gc);
    }
}

