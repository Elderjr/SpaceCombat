package server.user;

import server.data.User;
import server.room.battle.PersonalStatistic;

public class UserDAO {

    public static User login(String username, String password) {
        return new User(username.length(), username);
    }

    public static void updateStatistic(PersonalStatistic statistic, boolean wins, boolean draw) {
        if(wins){
            System.out.println("atualizando: " + statistic.getUser().getUsername() + "(ganhou)");
        }else if(draw){
            System.out.println("atualizando: " + statistic.getUser().getUsername() + "(empatou)");
        }else{
            System.out.println("atualizando: " + statistic.getUser().getUsername() + "(perdeu)");
        }
        
    }
}
