package client.sprite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import javafx.scene.image.Image;
import server.actors.ActorsTypes;
import server.serverConstants.ServerConstants;

public class ExternalFileLoader {

    private static ExternalFileLoader instance = new ExternalFileLoader();
    private HashMap<String, Spritesheet> spritesheets;

    private ExternalFileLoader() {
        this.spritesheets = new HashMap<>();
    }

    public static ExternalFileLoader getInstance() {
        return instance;
    }

    public Sprite getSprite(String actorType, int team) {
        String spritesheetName = actorType;
        if (team == ServerConstants.BLUE_TEAM) {
            spritesheetName += "_blue";
        } else if (team == ServerConstants.RED_TEAM) {
            spritesheetName += "_red";
        } else {
            return null;
        }
        return new Sprite(getSpritesheet(spritesheetName));
    }

    private Spritesheet getSpritesheet(String spritesheetName) {
        if (spritesheets.containsKey(spritesheetName)) {
            return spritesheets.get(spritesheetName);
        } else {
            Spritesheet spritesheet = loadSpritesheet(spritesheetName);
            if (spritesheet != null) {
                spritesheets.put(spritesheetName, spritesheet);
            }
            return spritesheet;
        }
    }

    private Spritesheet loadSpritesheet(String spritesheetName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("images/" + spritesheetName + ".txt")));
            int totalLines = Integer.parseInt(br.readLine().split("=")[1]);
            int totalColumns = Integer.parseInt(br.readLine().split("=")[1]);
            Image img = new Image(getClass().getResourceAsStream("images/" + spritesheetName + ".png"));
            br.close();
            return new Spritesheet(img, totalLines, totalColumns);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Nao foi possivel carregar a spritesheet: " + spritesheetName);
        return null;
    }
}
