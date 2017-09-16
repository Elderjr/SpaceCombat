/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.actors;

import java.io.Serializable;

/**
 *
 * @author elderjr
 */
public class Position implements Serializable{
    
    private double x;
    private double y;
    
    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    
    public void updatePosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
}
