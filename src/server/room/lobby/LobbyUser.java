package server.room.lobby;

import server.user.SimpleUser;

public class LobbyUser {

    private SimpleUser user;
    private String spaceshipSelected;
    private boolean confirmed;

    public LobbyUser(SimpleUser user, String spaceshipSelected, boolean confirmed) {
        this.user = user;
        this.spaceshipSelected = spaceshipSelected;
        this.confirmed = confirmed;
    }

    public SimpleUser getUser() {
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
