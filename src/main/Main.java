/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import client.windows.GameWindow;


/**
 *
 * @author elderjr
 */
public class Main {
    public static void main(String args[]){
        GameWindow.start();
        /*
        if(args.length == 0 || args[0].equalsIgnoreCase("-client")){
            GameWindow.start();
        }else if(args.length >= 2 && args[0].equalsIgnoreCase("-server")){
            server.ServerEngine.start(args[1]);
        }else{
            System.out.println("Command not recognized, use -client to start client or -server ip to start server");
        }
        */
    }
}
