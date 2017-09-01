/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.exceptions;

/**
 *
 * @author elderjr
 */
public class NotLoggedException extends Exception{
    
    public NotLoggedException(){
        
    }
    
    @Override
    public String getMessage(){
        return "Not Logged";
    }
}
