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
public abstract class LoadDataScene extends GameScene{
    
    private static final int TOLERANCE = 10;
    private Thread thread;
    private long fetchTime;
    private int errorsCount;
    
    public LoadDataScene(GameContext context, long fetchTime) {
        super(context);
        this.fetchTime = fetchTime;
        this.errorsCount = 0;
        initThread();
    }
    
    public LoadDataScene(GameContext context, Image background, long fetchTime) {
        super(context, background);
        this.fetchTime = fetchTime;
        this.errorsCount = 0;
        initThread();
    }
    
    public abstract void loadData() throws RemoteException, NotLoggedException;
    
    @Override
    public void changeScene(GameScene scene){
        super.changeScene(scene);
        this.thread.stop();
    }
    
    private void incrementErrorCount(){
        this.errorsCount++;
        if(this.errorsCount == TOLERANCE){
            changeScene(new MainScene(getContext(), MainScene.CONNECTION_ERROR));
        }
    }
    
    public void initThread(){
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        loadData();
                        errorsCount = 0;
                    } catch (RemoteException ex) {
                        System.out.println("Connection down: " + ex.getMessage());
                        incrementErrorCount();
                    } catch (NotLoggedException ex) {
                        changeScene(new MainScene(getContext(), MainScene.NOTLOGGED_ERROR));
                    } catch(Exception ex){
                        System.out.println("An unexpected error occured: " + ex.getMessage());
                        incrementErrorCount();
                    }
                }
            }
        });
        this.thread.start();
    }
}
