/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import client.windows.GameWindow;
import javax.swing.JOptionPane;
import server.dao.ConnectionFactory;


/**
 *
 * @author elderjr
 */
public class Main {
    public static void main(String args[]){
        if(args.length == 0 || args[0].equalsIgnoreCase("-client")){
            GameWindow.start();
        }else if(args.length > 0 && args[0].equalsIgnoreCase("-server")){
            String host = args.length >= 2 ? args[1] : "127.0.0.1";
            if(args.length >= 3){
                ConnectionFactory.getInstance().setUser(args[2]);
            }
            if(args.length >= 4){
                ConnectionFactory.getInstance().setPassword(args[3]);
            }
            server.ServerEngine.start(host);
        }else{
            System.out.println("Command not recognized");
            System.out.println("use -client to start the client");
            System.err.println("use -server IP DATABASE_USER PASSWORD_USER to start the server");
        }
    }
}
