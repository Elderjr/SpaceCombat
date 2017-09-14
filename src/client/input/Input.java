/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.input;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author elderjr
 */
public class Input {
 
    public static final int MOUSE_PRESSED = 0;
    public static final int MOUSE_RELEASED = 1;
    
    private Set<String> keysPressed;
    private int mouseState;
    private double mouseX;
    private double mouseY;
    
    public Input(){
        this.keysPressed = new HashSet<>();
        this.mouseState = MOUSE_RELEASED;
        this.mouseX = 0.0;
        this.mouseY = 0.0;
    }
    
    public void mousePressed(double x, double y){
        this.mouseState = MOUSE_PRESSED;
        this.mouseX = x;
        this.mouseY = y;
    }
    
    public void mouseReleased(double x, double y){
        this.mouseState = MOUSE_RELEASED;
        this.mouseX = x;
        this.mouseY = y;
    }
    
    public void keyPressed(String keyName){
        this.keysPressed.add(keyName);
    }
    
    public void keyReleased(String keyName){
        this.keysPressed.remove(keyName);
    }
    
    public boolean containsKey(String keyName){
        return this.keysPressed.contains(keyName);
    }
    
    public double getMouseX(){
        return this.mouseX;
    }
    
    public double getMouseY(){
        return this.mouseY;
    }
    
    public int getMouseState(){
        return this.mouseState;
    }
}
