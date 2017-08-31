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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import server.data.RoomData;
import server.room.SimpleRoom;

public final class RoomScene extends GameScene {

    private static final int maxRoomsPerPage = 8;
    private RoomData roomData;
    private Thread roomThread;
    private List<RoomButton> roomButtons;
    private int currentPage;

    public RoomScene(GameContext context) {
        super(context);
        initComponents();
        this.roomData = ClientNetwork.getInstance().getRooms();
        this.roomButtons = new LinkedList<>();
        this.currentPage = 1;
        initThread();
    }

    public void initComponents() {
        Button btPreviousPage = new Button(340, 200, "<", 30, 30, new ActionPerfomed() {
            @Override
            public void doAction() {
                if (currentPage > 1) {
                    currentPage--;
                }
            }
        });
        Button btNextPage = new Button(400, 200, ">", 30, 30, new ActionPerfomed() {
            @Override
            public void doAction() {
                if ((currentPage + 1) * maxRoomsPerPage < roomButtons.size()) {
                    currentPage++;
                }

            }
        });
        addComponents(btNextPage, btPreviousPage);
    }

    public void initThread() {
        this.roomThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        roomData = ClientNetwork.getInstance().getRooms();
                        createRoomButtons();
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoomScene.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        this.roomThread.start();
    }

    private void createRoomButtons() {
        synchronized (roomButtons) {
            int x = 50;
            int y = 50;
            int count = 0;
            roomButtons.clear();
            for (SimpleRoom r : this.roomData.getRooms()) {
                roomButtons.add(new RoomButton(x, y, r, new ActionPerfomed() {
                    @Override
                    public void doAction() {
                        SimpleRoom room = ClientNetwork.getInstance().enterRoom(r.getId());
                        roomThread.stop();
                        changeScene(new LobbyScene(getContext(), room));
                    }
                }));
                count++;
                if (count == maxRoomsPerPage) {
                    x = 50;
                    y = 50;
                    count = 0;
                } else if (count % 2 == 0) {
                    y += 120;
                    x = 50;
                } else {
                    x += 120;
                }
            }
        }

    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
        gc.setFill(Color.GREEN);
        gc.fillText("" + currentPage, 380, 200);
        synchronized (roomButtons) {
            int init = (currentPage - 1) * maxRoomsPerPage;
            int end = init + maxRoomsPerPage;
            for (int i = init; i < end && i < roomButtons.size(); i++) {
                roomButtons.get(i).render(gc);
            }
        }
    }

    @Override
    public void update(Input input) {
        super.update(input);
        synchronized (roomButtons) {
            int init = (currentPage - 1) * maxRoomsPerPage;
            int end = init + maxRoomsPerPage;
            if (input.getMouseState() == Input.MOUSE_PRESSED) {
                for (int i = init; i < end && i < roomButtons.size(); i++) {
                    roomButtons.get(i).mousePressed(input.getMouseX(), input.getMouseY());
                }
            } else if (input.getMouseState() == Input.MOUSE_RELEASED) {
                for (int i = init; i < end && i < roomButtons.size(); i++) {
                    roomButtons.get(i).mouseReleased();
                }
            }
        }
    }
}
