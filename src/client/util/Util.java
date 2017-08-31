/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.util;

/**
 *
 * @author elderjr
 */
public class Util {

    public static String formatMicroseconds(long ms) {
        long minutes = ms / 60000;
        long seconds = (ms % 60000) / 1000;
        if (seconds >= 10) {
            return minutes + ":" + seconds;
        } else {
            return minutes + ":0" + seconds;
        }
    }
}
