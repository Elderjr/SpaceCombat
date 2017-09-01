package server.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;


public class LobbyData implements Serializable {

    private HashMap<Long, LobbyUser> blueTeam;
    private HashMap<Long, LobbyUser> redTeam;

    public LobbyData(HashMap<Long, LobbyUser> blueTeam, HashMap<Long, LobbyUser> redTeam) {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
    }

    public boolean isAllReady(int playersPerTeam) {
        if (blueTeam.size() == playersPerTeam && redTeam.size() == playersPerTeam) {
            for (LobbyUser user : getBlueTeam()) {
                if (!user.isConfirmed()) {
                    return false;
                }
            }
            for (LobbyUser user : getRedTeam()) {
                if (!user.isConfirmed()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Collection<LobbyUser> getBlueTeam() {
        return this.blueTeam.values();
    }

    public Collection<LobbyUser> getRedTeam() {
        return this.redTeam.values();
    }
}
