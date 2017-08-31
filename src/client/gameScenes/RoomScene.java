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
import client.gui.RoomButton;
import client.input.Input;
import client.windows.GameContext;
import javafx.scene.canvas.GraphicsContext;
import client.network.ClientNetwork;
import java.rmi.RemoteException;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
import server.data.RoomData;
import server.room.SimpleRoom;
import server.user.User;

public final class RoomScene extends GameScene {

    private static final int ROOMS_PER_PAGE = 2;
    private RoomData roomData;
    private RoomButton pageButtons[];
    private long lastUpdate;
    private int currentPage;

    public RoomScene(GameContext context) {
        super(context);
        initComponents();
        this.pageButtons = new RoomButton[ROOMS_PER_PAGE];
        this.currentPage = 1;
        this.lastUpdate = 0;
    }

    public void initComponents() {
        Button btPreviousPage = new Button(340, 200, "<", 30, 30, new ActionPerfomed() {
            @Override
            public void doAction() {
                if (currentPage > 1) {
                    updatePageButtons(currentPage - 1);
                }
            }
        });
        Button btNextPage = new Button(400, 200, ">", 30, 30, new ActionPerfomed() {
            @Override
            public void doAction() {
                updatePageButtons(currentPage + 1);

            }
        });
        Button btCreateRoom = new Button(500, 200, "Create Room", 60, 30, new ActionPerfomed() {
            @Override
            public void doAction() {
                String roomName = JOptionPane.showInputDialog("Room Name");
                int playerPerTeam = Integer.parseInt(JOptionPane.showInputDialog("Players Per Team"));
                long time = Long.parseLong(JOptionPane.showInputDialog("Match Time (m): ")) * 60000;
                try {
                    SimpleRoom room = ClientNetwork.getInstance().createRoom(playerPerTeam, time, roomName);
                    if (room != null) {
                        changeScene(new LobbyScene(getContext(), room));
                    } else {
                        JOptionPane.showMessageDialog(null, "Error in create room");
                    }
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), true));
                }
            }
        });
        addComponents(btNextPage, btPreviousPage, btCreateRoom);
    }

    public void updatePageButtons(int page) {
        if (this.roomData != null) {
            int init = (page - 1) * ROOMS_PER_PAGE;
            int end = init + ROOMS_PER_PAGE;
            int index = 0;
            int cont = 0;
            int x = 50;
            int y = 50;
            for (int i = init; i < end; i++) {
                this.pageButtons[index] = null;
                if (i < roomData.getRooms().size()) {
                    SimpleRoom room = roomData.getRooms().get(i);
                    this.pageButtons[index] = new RoomButton(x, y, room, new ActionPerfomed() {
                        @Override
                        public void doAction() {
                            try {
                                ClientNetwork.getInstance().enterRoom(room.getId());
                                changeScene(new LobbyScene(getContext(), room));
                            } catch (RemoteException ex) {
                                changeScene(new MainScene(getContext(), true));
                            }
                        }
                    });
                    cont++;
                    if (cont % 2 == 0) {
                        x = 50;
                        y += 120;
                    } else {
                        x += 120;
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
        gc.setFill(Color.GREEN);
        gc.fillText("" + currentPage, 380, 200);
        for (RoomButton bt : this.pageButtons) {
            if (bt != null) {
                bt.render(gc);
            }
        }
        if (this.roomData != null) {
            int y = 30;
            for (User user : this.roomData.getOnlineUsers()) {
                gc.fillText(user.getUsername(), 400, y);
                y += 40;
            }
        }
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
        if (System.currentTimeMillis() - this.lastUpdate >= 1000) { //10s
            try {
                this.roomData = ClientNetwork.getInstance().getRooms();
                updatePageButtons(this.currentPage);
                this.lastUpdate = System.currentTimeMillis();
            } catch (RemoteException ex) {
                changeScene(new MainScene(getContext(), true));
            }
        }
    }
}
