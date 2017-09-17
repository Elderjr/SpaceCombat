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
import client.input.ExternalFileLoader;
import java.rmi.RemoteException;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.data.LobbyData;
import server.room.SimpleRoom;
import server.data.LobbyUser;
import server.exceptions.NotLoggedException;
import constants.Constants;

public final class LobbyScene extends LoadDataScene {

    private static final Font PING_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 15);
    private static final Font TITLE_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 30);

    private final SimpleRoom room;
    private final LobbyUserPanel bluePanels[];
    private final LobbyUserPanel redPanels[];
    private final long matchTime;
    private LobbyData data;
    private Thread roomThread;
    private long ping;
    private long lastPing;

    public LobbyScene(GameContext context, SimpleRoom room) {
        super(context, ExternalFileLoader.getInstance().getImage("background.png"), 10);
        this.room = room;
        this.matchTime = room.getMathTime() / 60000;
        this.bluePanels = new LobbyUserPanel[5];
        this.redPanels = new LobbyUserPanel[5];
        this.ping = 0;
        this.lastPing = 0;
        initLobbyUserPanels();
        initComponents();
    }

    public void initLobbyUserPanels() {
        int x = 40;
        int y = 70;
        for (int i = 0; i < this.bluePanels.length; i++) {
            this.bluePanels[i] = new LobbyUserPanel(x, y, Constants.BLUE_TEAM);
            this.redPanels[i] = new LobbyUserPanel(x, y + 295, Constants.RED_TEAM);
            addComponents(this.bluePanels[i], this.redPanels[i]);
            x += 150;
        }
    }

    public void initSpaceshiptsButtons() {
        Image defaultImage = ExternalFileLoader.getInstance().getImage("btAssaulter.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("btAssaulter_onclick.png");
        Button btSelectAssaulter = new ImageButton(40, 285, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), Constants.SPACESHIP_ASSAULTER);
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }

            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("btSupporter.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("btSupporter_onclick.png");
        Button btSelectSupporter = new ImageButton(190, 285, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), Constants.SPACESHIP_SUPPORTER);
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                } catch (NotLoggedException ex) {
                    changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                }
            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("btRaptor.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("btRaptor_onclick.png");
        Button btSelectRaptor = new ImageButton(340, 285, defaultImage, onclickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), Constants.SPACESHIP_RAPTOR);
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
        Image defaultImage = ExternalFileLoader.getInstance().getImage("btChangeTeam.png");
        Image onclickImage = ExternalFileLoader.getInstance().getImage("btChangeTeam_onclick.png");
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

        defaultImage = ExternalFileLoader.getInstance().getImage("btLetsGo.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("btLetsGo_onclick.png");
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
        defaultImage = ExternalFileLoader.getInstance().getImage("btExitRoom.png");
        onclickImage = ExternalFileLoader.getInstance().getImage("btExitRoom_onclick.png");
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

    @Override
    public void loadData() throws RemoteException, NotLoggedException {
        LobbyData buffer;
        if (System.currentTimeMillis() - this.lastPing >= 1000) {
            long pingAux = System.currentTimeMillis();
            buffer = ClientNetwork.getInstance().getLobbyData(this.room.getId());
            this.ping = System.currentTimeMillis() - pingAux;
            this.lastPing = System.currentTimeMillis();
        } else {
            buffer = ClientNetwork.getInstance().getLobbyData(this.room.getId());
        }
        if (this.data != null) {
            synchronized (this.data) {
                this.data = buffer;
            }
        } else {
            this.data = buffer;
        }
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
        gc.setFill(Color.YELLOW);
        gc.setFont(PING_FONT);
        gc.fillText("ping: " + this.ping + "ms", 20, 15);
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
