/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gameScenes;

/**
 *
 * @author elderjr
 */
import client.gui.ActionPerfomed;
import client.gui.Button;
import client.gui.ImageButton;
import client.gui.LobbyUserPanel;
import client.input.Input;
import javafx.scene.canvas.GraphicsContext;
import client.network.ClientNetwork;
import client.sprite.ExternalFileLoader;
import java.rmi.RemoteException;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.actors.ActorsTypes;
import server.data.LobbyData;
import server.room.SimpleRoom;
import server.data.LobbyUser;
import server.exceptions.NotLoggedException;
import server.serverConstants.ServerConstants;

public final class LobbyScene extends GameScene {

    private static final Font TITLE_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 30);
    private final SimpleRoom room;
    private final LobbyUserPanel bluePanels[];
    private final LobbyUserPanel redPanels[];
    private final long matchTime;
    private LobbyData data;
    private Thread roomThread;

    public LobbyScene(GameContext context, SimpleRoom room) {
        super(context, ExternalFileLoader.getInstance().getImage("client/images/background.jpg"));
        this.room = room;
        this.matchTime = room.getMathTime() / 60000;
        this.bluePanels = new LobbyUserPanel[5];
        this.redPanels = new LobbyUserPanel[5];
        initLobbyUserPanels();
        initComponents();
        initThread();
    }

    @Override
    public void changeScene(GameScene scene){
        super.changeScene(scene);
        this.roomThread.stop();
    }
    
    public void initLobbyUserPanels() {
        int x = 40;
        int y = 70;
        for (int i = 0; i < this.bluePanels.length; i++) {
            this.bluePanels[i] = new LobbyUserPanel(x, y, ServerConstants.BLUE_TEAM);
            this.redPanels[i] = new LobbyUserPanel(x, y + 295, ServerConstants.RED_TEAM);
            addComponents(this.bluePanels[i], this.redPanels[i]);
            x += 150;
        }
    }

    public void initSpaceshiptsButtons() {
        Image defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btAssaulter.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btAssaulter_onclick.png");
        Button btSelectAssaulter = new ImageButton(40, 285, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), ActorsTypes.SPACESHIP_ASSAULTER);
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }

            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btSupporter.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btSupporter_onclick.png");
        Button btSelectSupporter = new ImageButton(190, 285, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), ActorsTypes.SPACESHIP_SUPPORTER);
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }
            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btRaptor.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btRaptor_onclick.png");
        Button btSelectRaptor = new ImageButton(340, 285, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), ActorsTypes.SPACESHIP_RAPTOR);
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }
            }
        });
        addComponents(btSelectAssaulter, btSelectRaptor, btSelectSupporter);
    }

    public void initComponents() {
        initLobbyUserPanels();
        initSpaceshiptsButtons();
        Image defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btChangeTeam.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btChangeTeam_onclick.png");
        Button btChangeTeam = new ImageButton(490, 250, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeTeam(room.getId());
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }

            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btLetsGo.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btLetsGo_onclick.png");
        Button btChangeConfirm = new ImageButton(640, 250, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeConfirm(room.getId());
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }
            }
        });
        defaultImage = ExternalFileLoader.getInstance().getImage("client/images/btExitRoom.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("client/images/btExitRoom_onclick.png");
        Button btBack = new ImageButton(336, 545, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().exitRoom(room.getId());
                    changeScene(new RoomScene(getContext()));
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }
            }
        });
        addComponents(btChangeConfirm, btChangeTeam, btBack);
    }

    public void initThread() {
        this.roomThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = ClientNetwork.getInstance().getLobbyData(room.getId());
                    LobbyData buffer;
                    while (true) {
                        buffer = ClientNetwork.getInstance().getLobbyData(room.getId());
                        synchronized (data) {
                            data = buffer;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }
            }
        }
        );

        this.roomThread.start();
    }

    private void updateLobbyUsers() {
        for (int i = 0; i < this.bluePanels.length; i++) {
            this.bluePanels[i].setLobbyUser(null);
            this.redPanels[i].setLobbyUser(null);
        }
        int index = 0;
        for (LobbyUser lobbyUser : this.data.getBlueTeam()) {
            this.bluePanels[index].setLobbyUser(lobbyUser);
            index++;
        }
        index = 0;
        for (LobbyUser lobbyUser : this.data.getRedTeam()) {
            this.redPanels[index].setLobbyUser(lobbyUser);
            index++;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setFont(TITLE_FONT);
        gc.strokeText("Room #" + room.getId() + " - " + room.getName() + " - "
                + room.getMaxPlayersPerTeam() + " vs " + room.getMaxPlayersPerTeam() + " - "
                + this.matchTime + ":00 min", 40, 60);
        gc.fillText("Room #" + room.getId() + " - " + room.getName() + " - "
                + room.getMaxPlayersPerTeam() + " vs " + room.getMaxPlayersPerTeam() + " - "
                + this.matchTime + ":00 min", 40, 60);
        renderComponents(gc);
    }

    @Override
    public void update(Input input) {
        super.update(input);
        if (data != null) {
            synchronized (data) {
                if (data.isAllReady(room.getMaxPlayersPerTeam())) {
                    changeScene(new BattleScene(getContext(), room));
                } else {
                    updateLobbyUsers();
                }
            }
        }
    }

}
