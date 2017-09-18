/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gameScenes;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import server.exceptions.NotLoggedException;

/**
 *
 * @author elderjr
 */
public abstract class LoadDataScene extends GameScene {

    private static final int TOLERANCE = 10;
    private Thread thread;
    private long fetchTime;
    private long pingTime;
    private long ping;
    private long lastPing;
    private int errorsCount;

    public LoadDataScene(GameContext context, long fetchTime, long pingTime) {
        super(context);
        this.fetchTime = fetchTime;
        this.pingTime = pingTime;
        this.lastPing = 0;
        this.ping = 0;
        this.errorsCount = 0;
    }

    public LoadDataScene(GameContext context, Image background, long fetchTime, long pingTime) {
        super(context, background);
        this.fetchTime = fetchTime;
        this.pingTime = pingTime;
        this.lastPing = 0;
        this.ping = 0;
        this.errorsCount = 0;
    }

    public abstract void loadData() throws RemoteException, NotLoggedException;

    public abstract void processLoadedData();

    @Override
    public void changeScene(GameScene scene) {
        super.changeScene(scene);
        if(this.thread != null){
            this.thread.stop();
        }
    }

    public long getPing() {
        return this.ping;
    }

    private void incrementErrorCount() {
        this.errorsCount++;
        if (this.errorsCount == TOLERANCE) {
            changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
        }
    }

    public final void startLoadThread() {
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (System.currentTimeMillis() - lastPing >= pingTime) {
                            long pingAux = System.currentTimeMillis();
                            loadData();
                            ping = System.currentTimeMillis() - pingAux;
                            lastPing = System.currentTimeMillis();
                        } else {
                            loadData();
                        }
                        errorsCount = 0;
                    } catch (RemoteException ex) {
                        System.out.println("Connection down: " + ex.getMessage());
                        incrementErrorCount();
                    } catch (NotLoggedException ex) {
                        changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                    } catch (Exception ex) {
                        System.out.println("An unexpected error occured: " + ex.getMessage());
                        incrementErrorCount();
                    }
                    processLoadedData();
                    try {
                        Thread.sleep(fetchTime);
                    } catch (InterruptedException ex) {
                        System.out.println("Interrupted Exception: " + ex.getMessage());
                    }
                }
            }
        });
        this.thread.start();
    }
}
