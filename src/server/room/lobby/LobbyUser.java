package server.room.lobby;

import java.io.Serializable;
import server.user.User;

public class LobbyUser implements Serializable{

    private User user;
    private String spaceshipSelected;
    private boolean confirmed;

    public LobbyUser(User user, String spaceshipSelected, boolean confirmed) {
        this.user = user;
        this.spaceshipSelected = spaceshipSelected;
        this.confirmed = confirmed;
    }

    public User getUser() {
        return this.user;
    }

    public void setSpaceshipSelected(String actorType) {
        this.spaceshipSelected = actorType;
    }

    public String getSpaceshipSelected() {
        return this.spaceshipSelected;
    }

    public void changeConfirm() {
        this.confirmed = !this.confirmed;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }
}
