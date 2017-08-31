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
import client.commands.ClientCommands;
import client.gui.Animation;
import client.input.Input;
import client.windows.GameContext;
import javafx.scene.canvas.GraphicsContext;
import client.network.ClientNetwork;
import client.sprite.ExternalFileLoader;
import client.sprite.Sprite;
import client.util.Util;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import server.actors.SimpleActor;
import server.data.BattleData;
import server.room.SimpleRoom;

public final class BattleScene extends GameScene {

    private SimpleRoom room;
    private HashMap<Long, Animation> animations;
    private BattleData data;
    private Thread battleThread;

    public BattleScene(GameContext context, SimpleRoom room) {
        super(context);
        this.animations = new HashMap<>();
        this.room = room;
        initThread();
    }

    public void initThread() {
        this.battleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = ClientNetwork.getInstance().getBattleData(room.getId());
                    while (data != null) {
                        try {
                            Thread.sleep(100);
                            synchronized (data) {
                                data = ClientNetwork.getInstance().getBattleData(room.getId());
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RoomScene.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (RemoteException ex) {
                    changeScene(new MainScene(getContext(), true));
                }

            }
        });
        this.battleThread.start();
    }

    private void updateAnimations(HashMap<Long, SimpleActor> simpleActors) {
        for (SimpleActor simpleActor : simpleActors.values()) {
            if (this.animations.containsKey(simpleActor.getId())) {
                Animation animation = this.animations.get(simpleActor.getId());
                animation.updateLocation(simpleActor.getLocation().x, simpleActor.getLocation().y);
                animation.setSpriteDirection(simpleActor.getDirection());
            } else {
                Sprite sprite = ExternalFileLoader.getInstance().
                        getSprite(simpleActor.getActorType(), simpleActor.getTeam());
                Animation animation = new Animation(simpleActor.getLocation().x,
                        simpleActor.getLocation().y,
                        sprite, (int) simpleActor.getSize().getWidth() / 2,
                        (int) simpleActor.getSize().getHeight() / 2);
                animation.setSpriteDirection(simpleActor.getDirection());
                this.animations.put(simpleActor.getId(), animation);
            }
        }
        Iterator<Long> keysIterator = this.animations.keySet().iterator();
        Long id = null;
        while (keysIterator.hasNext()) {
            id = keysIterator.next();
            if (!simpleActors.containsKey(id)) {
                keysIterator.remove();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        renderComponents(gc);
        for (Animation animation : this.animations.values()) {
            animation.render(gc);
        }
        if (data != null) {
            synchronized (data) {
                gc.setFill(Color.CYAN);
                gc.fillText("HP: " + data.getMyHp(), 30, 30);
                if (data.canUseShot()) {
                    gc.fillText("Shot +", 300, 30);
                } else {
                    gc.fillText("Shot -", 300, 30);
                }
                if (data.canUseSkill()) {
                    gc.fillText("Skill +", 340, 30);
                } else {
                    gc.fillText("Skill -", 340, 30);
                }
                gc.setFill(Color.BLUE);
                gc.fillText("" + data.getBlueTeamPoint(), 150, 30);
                gc.setFill(Color.WHITE);
                gc.fillText(Util.formatMicroseconds(data.getMatchTime()), 200, 30);
                gc.setFill(Color.RED);
                gc.fillText("" + data.getRedTeamPoint(), 250, 30);
            }
        }
    }

    private void processKeysPressed(Input input) {
        try {
            if (input.containsKey(KeyCode.W.getName()) && input.containsKey(KeyCode.D.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.UP_RIGHT);
            } else if (input.containsKey(KeyCode.W.getName()) && input.containsKey(KeyCode.A.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.UP_LEFT);
            } else if (input.containsKey(KeyCode.S.getName()) && input.containsKey(KeyCode.D.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.DOWN_RIGHT);
            } else if (input.containsKey(KeyCode.S.getName()) && input.containsKey(KeyCode.A.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.DOWN_LEFT);
            } else if (input.containsKey(KeyCode.W.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.UP);
            } else if (input.containsKey(KeyCode.A.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.LEFT);
            } else if (input.containsKey(KeyCode.S.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.DOWN);
            } else if (input.containsKey(KeyCode.D.getName())) {
                ClientNetwork.getInstance().move(room.getId(), ClientCommands.RIGHT);
            }
            if (input.containsKey(KeyCode.J.getName())) {
                ClientNetwork.getInstance().useShot(room.getId());
            }
            if (input.containsKey(KeyCode.K.getName())) {
                ClientNetwork.getInstance().useSkill(room.getId());
            }
        } catch (RemoteException ex) {
            this.battleThread.stop();
            changeScene(new MainScene(getContext(), true));
        }

    }

    @Override
    public void update(Input input) {
        super.update(input);
        processKeysPressed(input);
        try {
            if (data != null) {
                synchronized (data) {
                    if (data.getMatchTime() > 0) {
                        updateAnimations(data.getActors());
                    } else {
                        changeScene(new StatisticScene(getContext(), ClientNetwork.getInstance().getBattleStatistic(room.getId())));
                    }
                }
            } else {
                changeScene(new StatisticScene(getContext(), ClientNetwork.getInstance().getBattleStatistic(room.getId())));
            }
        } catch (RemoteException ex) {
            this.battleThread.stop();
            changeScene(new MainScene(getContext(), true));
        }

    }

}
