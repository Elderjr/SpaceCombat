/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.sprite.ExternalFileLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import server.actors.ActorsTypes;
import server.data.LobbyUser;
import server.serverConstants.ServerConstants;

/**
 *
 * @author elderjr
 */
public class LobbyUserPanel extends Component {

    private static final Font DEFAULT_FONT = Font.font("Serif", FontWeight.EXTRA_BOLD,
            FontPosture.REGULAR, 15);
    private Animation assaulterSpaceship;
    private Animation supporterSpaceship;
    private Animation raptorSpaceship;
    private final int team;
    private final Image panelReady;
    private final Image panelNotReady;

    private LobbyUser lobbyUser;

    public LobbyUserPanel(int x, int y, int team) {
        super(x, y);
        this.team = team;
        if (this.team == ServerConstants.BLUE_TEAM) {
            this.panelReady = ExternalFileLoader.getInstance().getImage("client/images/bluePanel_ready.png");
            this.panelNotReady = ExternalFileLoader.getInstance().getImage("client/images/bluePanel_notReady.png");
        } else {
            this.panelReady = ExternalFileLoader.getInstance().getImage("client/images/redPanel_ready.png");
            this.panelNotReady = ExternalFileLoader.getInstance().getImage("client/images/redPanel_notReady.png");
        }
        initAnimations();
    }

    public void setLobbyUser(LobbyUser lobbyUser) {
        this.lobbyUser = lobbyUser;
    }

    private final void initAnimations() {
        int x = getX() + 25;
        int y = getY() + 45;
        this.assaulterSpaceship = new Animation(x, y,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_ASSAULTER, team));
        this.supporterSpaceship = new Animation(x, y,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_SUPPORTER, team));
        this.raptorSpaceship = new Animation(x, y,
                ExternalFileLoader.getInstance().getSprite(
                        ActorsTypes.SPACESHIP_RAPTOR, team));
    }

    @Override
    public void render(GraphicsContext gc) {
        if (this.lobbyUser != null) {
            gc.setFont(DEFAULT_FONT);
            if (this.lobbyUser.isConfirmed()) {
                gc.drawImage(this.panelReady, getX(), getY());
            } else {
                gc.drawImage(this.panelNotReady, getX(), getY());
            }
            gc.setFill(Color.WHITE);
            gc.fillText(this.lobbyUser.getUser().getUsername(), getX() + 10, getY() + 15);
            if (this.lobbyUser.getSpaceshipSelected() == ActorsTypes.SPACESHIP_ASSAULTER) {
                this.assaulterSpaceship.render(gc);
            } else if (this.lobbyUser.getSpaceshipSelected() == ActorsTypes.SPACESHIP_RAPTOR) {
                this.raptorSpaceship.render(gc);
            } else if (this.lobbyUser.getSpaceshipSelected() == ActorsTypes.SPACESHIP_SUPPORTER) {
                this.supporterSpaceship.render(gc);
            }
        } else {
            gc.drawImage(this.panelNotReady, getX(), getY());
        }
    }

}