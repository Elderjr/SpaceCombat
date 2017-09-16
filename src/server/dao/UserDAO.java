package server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.data.GeneralStatistics;
import server.data.User;
import server.room.battle.PersonalStatistic;

public class UserDAO {

    public static void createUserTable() throws SQLException {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS user("
                    + "id BIGINT NOT NULL AUTO_INCREMENT primary key,"
                    + "username VARCHAR(30) NOT NULL UNIQUE,"
                    + "password VARCHAR(30) NOT NULL,"
                    + "matches INT NOT NULL DEFAULT 0,"
                    + "wins INT NOT NULL DEFAULT 0,"
                    + "loses INT NOT NULL DEFAULT 0,"
                    + "draws INT NOT NULL DEFAULT 0,"
                    + "kills INT NOT NULL DEFAULT 0,"
                    + "deaths INT NOT NULL DEFAULT 0"
                    + ");";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    public static boolean checkUsername(String username) throws SQLException {
        boolean userExists = false;
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = "Select id from user where username=? LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    userExists = rs.next();
                }
            }
        }
        return userExists;
    }

    public static User registerUser(String username, String password) throws SQLException {
        if (!checkUsername(username)) {
            try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
                String sql = "INSERT INTO user(username,password)"
                        + " values (?,?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.executeUpdate();
                }
                return UserDAO.login(username, password);
            }
        }
        return null;
    }

    public static GeneralStatistics getUserStatistics(long userId) throws SQLException {
        GeneralStatistics generalStatistics = null;
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = "Select matches, wins, loses, draws, kills, deaths "
                    + "from user where id = ? LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int matches = rs.getInt("matches");
                        int wins = rs.getInt("wins");
                        int loses = rs.getInt("loses");
                        int draws = rs.getInt("draws");
                        int kills = rs.getInt("kills");
                        int deaths = rs.getInt("deaths");
                        return new GeneralStatistics(matches, wins, draws, loses, kills, deaths);
                    }
                }
            }
        }
        return generalStatistics;
    }

    public static User login(String username, String password) throws SQLException {
        User user = null;
        String sql = "Select id from user where username=? and password=? LIMIT 1";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        long id = rs.getLong("id");
                        user = new User(id, username);
                    }
                }
            }
        }
        return user;
    }

    public static void updateStatistic(PersonalStatistic statistic, boolean wins, boolean draw) throws SQLException {
        String sqlUpdate = "UPDATE user SET matches = matches + 1, "
                + "kills = kills + " + statistic.getKills()
                + ", deaths = deaths + " + statistic.getDeaths() + ", ";
        if (wins) {
            sqlUpdate += "wins = wins + 1 ";
        } else if (draw) {
            sqlUpdate += "draws = draws + 1 ";
        } else {
            sqlUpdate += "loses = loses + 1 ";
        }
        sqlUpdate += "WHERE id = " + statistic.getUser().getId();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlUpdate);
        }
    }
}
