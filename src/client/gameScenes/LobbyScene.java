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
import client.gui.Animation;
import client.gui.Button;
import client.input.Input;
import client.windows.GameContext;
import javafx.scene.canvas.GraphicsContext;
import client.network.ClientNetwork;
import client.sprite.ExternalFileLoader;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.actors.ActorsTypes;
import server.data.LobbyData;
import server.room.SimpleRoom;
import server.room.lobby.LobbyUser;
import server.serverConstants.ServerConstants;

public final class LobbyScene extends GameScene {

    private final SimpleRoom room;
    private LobbyData data;
    private Thread roomThread;
    private Animation assaulterSpaceship[];
    private Animation supporterSpaceship[];
    private Animation raptorSpaceship[];

    public LobbyScene(GameContext context, SimpleRoom room) {
        super(context);
        this.room = room;
        initAnimations();
        initComponents();
        initThread();
    }

    public void initAnimations() {
        this.assaulterSpaceship = new Animation[2];
        this.supporterSpaceship = new Animation[2];
        this.raptorSpaceship = new Animation[2];
        this.assaulterSpaceship[0] = new Animation(100, 450,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_ASSAULTER, ServerConstants.BLUE_TEAM));
        this.assaulterSpaceship[1] = new Animation(164, 450,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_ASSAULTER, ServerConstants.RED_TEAM));
        this.supporterSpaceship[0] = new Animation(264, 450,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_SUPPORTER, ServerConstants.BLUE_TEAM));
        this.supporterSpaceship[1] = new Animation(328, 450,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_SUPPORTER, ServerConstants.RED_TEAM));
        this.raptorSpaceship[0] = new Animation(412, 450,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_RAPTOR, ServerConstants.BLUE_TEAM));
        this.raptorSpaceship[1] = new Animation(476, 450,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_RAPTOR, ServerConstants.RED_TEAM));

        Button btSelectAssaulter = new Button(100, 530, "Assaulter", 100, 40, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), ActorsTypes.SPACESHIP_ASSAULTER);
                } catch (RemoteException ex) {
                    roomThread.stop();
                    changeScene(new MainScene(getContext(), true));
                }

            }
        });

        Button btSelectSupporter = new Button(264, 530, "Supporter", 100, 40, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), ActorsTypes.SPACESHIP_SUPPORTER);
                } catch (RemoteException ex) {
                    roomThread.stop();
                    changeScene(new MainScene(getContext(), true));
                }
            }
        });

        Button btSelectRaptor = new Button(412, 530, "Raptor", 100, 40, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeSpacechip(room.getId(), ActorsTypes.SPACESHIP_RAPTOR);
                } catch (RemoteException ex) {
                    roomThread.stop();
                    changeScene(new MainScene(getContext(), true));
                }
            }
        });
        addComponents(this.assaulterSpaceship[0], this.assaulterSpaceship[1]);
        addComponents(this.supporterSpaceship[0], this.supporterSpaceship[1]);
        addComponents(this.raptorSpaceship[0], this.raptorSpaceship[1]);
        addComponents(btSelectAssaulter, btSelectRaptor, btSelectSupporter);
    }

    public void initComponents() {
        Button btChangeConfirm = new Button(164, 400, "Change Confirm", 120, 50, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeConfirm(room.getId());
                } catch (RemoteException ex) {
                    roomThread.stop();
                    changeScene(new MainScene(getContext(), true));
                }
            }
        });
        Button btChangeTeam = new Button(290, 400, "Change Team", 120, 50, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().changeTeam(room.getId());
                } catch (RemoteException ex) {
                    roomThread.stop();
                    changeScene(new MainScene(getContext(), true));
                }

            }
        });
        Button btBack = new Button(400, 400, "Back", 120, 50, new ActionPerfomed() {
            @Override
            public void doAction() {
                try {
                    ClientNetwork.getInstance().exitRoom(room.getId());
                    changeScene(new RoomScene(getContext()));
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), true));
                } finally {
                    roomThread.stop();
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
                    while (true) {
                        synchronized (data) {
                            data = ClientNetwork.getInstance().getLobbyData(room.getId());
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), true));
                    ex.printStackTrace();
                }
            }
        }
        );

        this.roomThread.start();
    }

    private void renderSpaceship(GraphicsContext gc, String spacehipType, int team, int x, int y) {
        Animation spaceship[] = null;
        if (spacehipType.equals(ActorsTypes.SPACESHIP_ASSAULTER)) {
            spaceship = this.assaulterSpaceship;
        } else if (spacehipType.equals(ActorsTypes.SPACESHIP_RAPTOR)) {
            spaceship = this.raptorSpaceship;
        } else if (spacehipType.equals(ActorsTypes.SPACESHIP_SUPPORTER)) {
            spaceship = this.supporterSpaceship;
        }
        if (team == ServerConstants.BLUE_TEAM) {
            spaceship[0].render(gc, x, y);
        } else if (team == ServerConstants.RED_TEAM) {
            spaceship[1].render(gc, x, y);
        }
    }

    private void renderUsers(GraphicsContext gc) {
        int x = 30;
        int y = 30;
        for (LobbyUser user : data.getBlueTeam()) {
            gc.strokeText(user.getUser().getUsername(), x, y);
            renderSpaceship(gc, user.getSpaceshipSelected(), ServerConstants.BLUE_TEAM, x, y + 10);
            if (user.isConfirmed()) {
                gc.strokeText("Ready", x, y + 84);
            }
            y += 120;
        }
        x = 230;
        y = 30;
        for (LobbyUser user : data.getRedTeam()) {
            gc.strokeText(user.getUser().getUsername(), x, y);
            renderSpaceship(gc, user.getSpaceshipSelected(), ServerConstants.RED_TEAM, x, y + 10);
            if (user.isConfirmed()) {
                gc.strokeText("Ready", x, y + 84);
            }
            y += 120;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
        if (data != null) {
            synchronized (data) {
                renderUsers(gc);
            }
        }
    }

    @Override
    public void update(Input input) {
        super.update(input);
        if (data != null) {
            synchronized (data) {
                if (data.isAllReady(room.getMaxPlayersPerTeam())) {
                    this.roomThread.stop();
                    changeScene(new BattleScene(getContext(), room));
                }
            }
        }
    }

}
