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
import client.gui.HpBar;
import client.gui.ImageLabel;
import client.input.Input;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.data.SimpleActor;
import server.data.BattleData;
import server.exceptions.NotLoggedException;
import server.room.SimpleRoom;
import server.room.battle.BattleStatistic;


public final class BattleScene extends GameScene {

    private static final Font TIME_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 20);
    private static final Font REGULAR_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 15);
    
    private final SimpleRoom room;
    private final HashMap<Long, Animation> animations;
    private BattleData data;
    private Thread battleThread;
    
    //Components
    private HpBar hpBar;
    private ImageLabel shootReady;
    private ImageLabel skillReady;

    public BattleScene(GameContext context, SimpleRoom room) {
        super(context, ExternalFileLoader.getInstance().getImage("client/images/battleBackground.png"));
        this.animations = new HashMap<>();
        this.room = room;
        this.loadData();
        this.initThread();
        initComponents();
    }

    public void initComponents(){
        this.hpBar = new HpBar(37, 53);
        this.hpBar.setMaxHP(this.data.getMaxHp());
        this.hpBar.setCurrentHP(this.data.getMyHp());
        this.shootReady = new ImageLabel(249, 78, ExternalFileLoader.getInstance().getImage("client/images/ready.png"));
        this.skillReady = new ImageLabel(345, 78, ExternalFileLoader.getInstance().getImage("client/images/ready.png"));
        addComponents(this.hpBar, this.shootReady, this.skillReady);
    }
    public void loadData() {
        try {
            BattleData buffer = ClientNetwork.getInstance().getBattleData(this.room.getId());
            if (data != null) {
                synchronized (this.data) {
                    this.data = buffer;
                }
            }else{
                this.data = buffer;
            }
        } catch (RemoteException ex) {
            changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
        } catch (NotLoggedException ex) {
            changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
        }
    }

    @Override
    public void changeScene(GameScene scene) {
        super.changeScene(scene);
        this.battleThread.stop();
    }

    public void initThread() {
        this.battleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (data != null) {
                    try {
                        Thread.sleep(100);
                        loadData();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoomScene.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        this.battleThread.start();
    }

    private void updateAnimations(HashMap<Long, SimpleActor> simpleActors) {
        for (SimpleActor simpleActor : simpleActors.values()) {
            if (this.animations.containsKey(simpleActor.getId())) {
                Animation animation = this.animations.get(simpleActor.getId());
                animation.updateLocation(simpleActor.getLocation().x + 25, simpleActor.getLocation().y + 110);
                animation.setSpriteDirection(simpleActor.getDirection());
            } else {
                Sprite sprite = ExternalFileLoader.getInstance().
                        getSprite(simpleActor.getActorType(), simpleActor.getTeam());
                Animation animation = new Animation(simpleActor.getLocation().x + 25,
                        simpleActor.getLocation().y + 110,
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
                gc.setFill(Color.WHITE);
                gc.setFont(TIME_FONT);
                gc.fillText(Util.formatMicroseconds(data.getMatchTime()), 380, 48);
                gc.setFill(Color.BLUE);
                gc.fillText("" + data.getBlueTeamPoint(), 585, 61);
                gc.setFill(Color.RED);
                gc.fillText("" + data.getRedTeamPoint(), 735, 61);
                gc.setFill(Color.WHITE);
                gc.setFont(REGULAR_FONT);
                gc.fillText("" + data.getKills(), 490, 92);
                gc.fillText("" + data.getDeaths(), 610, 92);
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
            changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
        } catch (NotLoggedException ex) {
            changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
        }

    }

    @Override
    public void update(Input input) {
        super.update(input);
        processKeysPressed(input);
        if (data != null) {
            synchronized (data) {
                if (data.getMatchTime() > 0) {
                    this.hpBar.setCurrentHP(this.data.getMyHp());
                    this.shootReady.setVisible(this.data.canUseShot());
                    this.skillReady.setVisible(this.data.canUseSkill());
                    updateAnimations(data.getActors());
                } else {
                    try {
                        BattleStatistic statistics = ClientNetwork.getInstance().getBattleStatistic(room.getId());
                        changeScene(new StatisticScene(getContext(), statistics));
                    } catch (RemoteException ex) {
                        changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
                    } catch (NotLoggedException ex) {
                        changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                    }
                }
            }
        } else {
            changeScene(new RoomScene(getContext()));
        }
    }

}
