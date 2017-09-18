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
import client.gui.RoomButton;
import client.input.Input;
import javafx.scene.canvas.GraphicsContext;
import client.network.ClientNetwork;
import client.input.ExternalFileLoader;
import client.windows.RoomForm;
import java.rmi.RemoteException;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.data.GeneralStatistics;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.data.User;
import server.exceptions.NotLoggedException;

public final class RoomScene extends LoadDataScene {

    private static final Font PING_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 15);
    private static final Font DEFAULT_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 20);
    private static final Font NUMBER_PAGE_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 40);

    private static final int ROOMS_PER_PAGE = 4;
    private final User user;
    private final RoomButton pageButtons[];
    private GeneralStatistics generalStatistics;
    private RoomData roomData;
    private int currentPage;

    public RoomScene(GameContext context) {
        super(context, ExternalFileLoader.getInstance().getImage("background.png"), 1000, 1000);
        initComponents();
        this.user = ClientNetwork.getInstance().getUser();
        this.pageButtons = new RoomButton[ROOMS_PER_PAGE];
        this.currentPage = 1;
        try {
            this.generalStatistics = ClientNetwork.getInstance().getGeneralStatistics();
        } catch (RemoteException ex) {
            changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
        } catch (NotLoggedException ex) {
            changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
        }
    }

    public void initComponents() {
        Image defaultImage = ExternalFileLoader.getInstance().getImage("left_button.png");
        Image onClickImage = ExternalFileLoader.getInstance().getImage("left_button_on_click.png");
        Button btPreviousPage = new ImageButton(250, 330, defaultImage, onClickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                if (currentPage > 1) {
                    updatePageButtons(currentPage - 1);
                }
            }
        });
        defaultImage = ExternalFileLoader.getInstance().getImage("right_button.png");
        onClickImage = ExternalFileLoader.getInstance().getImage("right_button_on_click.png");
        Button btNextPage = new ImageButton(350 + btPreviousPage.getWidth(), 330, defaultImage, onClickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                updatePageButtons(currentPage + 1);

            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("btCreateRoom.png");
        onClickImage = ExternalFileLoader.getInstance().getImage("btCreateRoom_onclick.png");
        Button btCreateRoom = new ImageButton(650, 360, defaultImage, onClickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                RoomForm roomForm = new RoomForm(null, true);
                roomForm.setVisible(true);
                roomForm.setFocusable(true);
                if (roomForm.getRoomName() != null) {
                    try {
                        SimpleRoom room = ClientNetwork.getInstance().createRoom(
                                roomForm.getMaxPlayersPerTeam(),
                                roomForm.getMatchTime(),
                                roomForm.getRoomName());
                        changeScene(new LobbyScene(getContext(), room));
                    } catch (RemoteException ex) {
                        changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                    } catch (NotLoggedException ex) {
                        changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                    }
                }
            }
        });

        defaultImage = ExternalFileLoader.getInstance().getImage("btExitRoom.png");
        onClickImage = ExternalFileLoader.getInstance().getImage("btExitRoom_onclick.png");
        Button btExit = new ImageButton(336, 545, defaultImage, onClickImage, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().exitGame();
                    changeScene(new MainScene(getContext()));
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                }
            }
        });
        addComponents(btNextPage, btPreviousPage, btCreateRoom, btExit);
    }

    public void updatePageButtons(int page) {
        if (this.roomData != null) {
            int init = (page - 1) * ROOMS_PER_PAGE;
            int end = init + ROOMS_PER_PAGE;
            int index = 0;
            int cont = 0;
            int x = 30;
            int y = 100;
            for (int i = init; i < end; i++) {
                this.pageButtons[index] = null;
                if (i < roomData.getRooms().size()) {
                    SimpleRoom room = roomData.getRooms().get(i);
                    this.pageButtons[index] = new RoomButton(x, y, room, new ActionPerfomed() {
                        @Override
                        public void doAction() {
                            try {
                                if (room.getMaxPlayersPerTeam() * 2 != room.getTotalPlayers()
                                        && room.getState() == constants.Constants.WAITING) {
                                    ClientNetwork.getInstance().enterRoom(room.getId());
                                    changeScene(new LobbyScene(getContext(), room));
                                }
                            } catch (RemoteException ex) {
                                changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                            } catch (NotLoggedException ex) {
                                changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                            }
                        }
                    });
                    cont++;
                    if (cont % 2 == 0) {
                        x = 30;
                        y += this.pageButtons[index].getHeight() + 15;
                    } else {
                        x += this.pageButtons[index].getWidth();
                    }
                }
                index++;
            }
            this.currentPage = page;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
        gc.setFill(Color.YELLOW);
        gc.setFont(PING_FONT);
        gc.fillText("ping: " + getPing() + "ms", 20, 15);
        gc.setFill(Color.WHITESMOKE);
        gc.setFont(DEFAULT_FONT);
        gc.fillText("User: [" + this.user.getUsername() + "]", 335, 60);
        gc.fillText(this.generalStatistics.toString(), 185, 85);
        gc.fillText("Online Players: ", 30, 400);
        if (this.roomData != null) {
            int x = 50;
            int y = 430;
            int index = 0;
            for (User user : this.roomData.getOnlineUsers()) {
                gc.fillText(user.getUsername(), x, y);
                index++;
                if (index % 7 == 0) {
                    x = 50;
                    y += 30;
                } else {
                    x += 100;
                }
            }
        }
        for (RoomButton bt : this.pageButtons) {
            if (bt != null) {
                bt.render(gc);
            }
        }
        gc.setFont(NUMBER_PAGE_FONT);
        gc.fillText("" + currentPage, 390, 380);
    }

    @Override
    public void update(Input input) {
        super.update(input);
        if (input.getMouseState() == Input.MOUSE_PRESSED) {
            for (RoomButton bt : this.pageButtons) {
                if (bt != null) {
                    bt.mousePressed(input.getMouseX(), input.getMouseY());
                }
            }
        } else if (input.getMouseState() == Input.MOUSE_RELEASED) {
            for (RoomButton bt : this.pageButtons) {
                if (bt != null) {
                    bt.mouseReleased();
                }
            }
        }
    }

    @Override
    public void loadData() throws RemoteException, NotLoggedException {
        this.roomData = ClientNetwork.getInstance().getRooms();
    }
    
    @Override
    public void processLoadedData(){
        updatePageButtons(this.currentPage);
    }
}
