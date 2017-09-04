package server;

import server.data.GeneralStatistics;
import server.data.User;
import server.room.battle.PersonalStatistic;

public final class LoggedUser {

    private final User user;
    private final GeneralStatistics statistics;
    private long lastCommand;

    public LoggedUser(User user, GeneralStatistics statistics) {
        this.user = user;
        this.statistics = statistics;
        updateLastCommand();
    }

    public User getUser() {
        return this.user;
    }

    public void incrementStatisticsValues(PersonalStatistic statistic, boolean win, boolean draw) {
        this.statistics.incrementValues(statistic, win, draw);
    }

    public void updateLastCommand() {
        this.lastCommand = System.currentTimeMillis();
    }

    public boolean isDisconnected(long limit) {
        return System.currentTimeMillis() - this.lastCommand >= limit;
    }
}
