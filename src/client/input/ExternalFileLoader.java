package client.input;

import client.ClientResource;
import client.sprite.Sprite;
import client.sprite.Spritesheet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import javafx.scene.image.Image;
import constants.Constants;

public class ExternalFileLoader {

    private static ExternalFileLoader instance = new ExternalFileLoader();
    private HashMap<String, Spritesheet> spritesheets;
    private HashMap<String, Image> images;

    private ExternalFileLoader() {
        this.spritesheets = new HashMap<>();
        this.images = new HashMap<>();
    }

    public static ExternalFileLoader getInstance() {
        return instance;
    }

    public Image getImage(String imageName) {
        if (this.images.containsKey(imageName)) {
            return this.images.get(imageName);
        } else {
            Image image = loadImage(imageName);
            this.images.put(imageName, image);
            return image;
        }
    }

    public Sprite getSprite(String actorType, int team) {
        String spritesheetName = actorType;
        if (team == Constants.BLUE_TEAM) {
            spritesheetName += "_blue";
        } else if (team == Constants.RED_TEAM) {
            spritesheetName += "_red";
        } else {
            return null;
        }
        return new Sprite(getSpritesheet(spritesheetName));
    }

    public Sprite getSprite(String spritesheetName) {
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

    private Image loadImage(String imageName) {
        return new Image("client/images/" + imageName);
    }

    private Spritesheet loadSpritesheet(String spritesheetName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ClientResource.class.getResourceAsStream("images/spritesheets/" + spritesheetName + ".txt")));
            int totalLines = Integer.parseInt(br.readLine().split("=")[1]);
            int totalColumns = Integer.parseInt(br.readLine().split("=")[1]);
            Image img = new Image("client/images/spritesheets/"+spritesheetName+".png");
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
