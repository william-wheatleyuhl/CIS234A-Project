package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DB class for the notification log
 *
 * @author Liana Schweitzer
 * @version 2019.04.29
 */
public class JavaneseJumpingBeansDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String LOAD_SQL = "SELECT TOP 200 * FROM NOTIFICATION ORDER BY DateTime DESC, MessageID DESC";
    private static final String NOTIFICATION_SQL = "SELECT * FROM NOTIFICATION " +
            "WHERE DateTime >= ? AND DateTime <= ? ORDER BY DateTime DESC, MessageID DESC";
    private static final String USER_SQL = "SELECT Username FROM [USER] WHERE UserID = ?";
    private static String sqlStringName;
    private static Timestamp minTmst;
    private static Timestamp maxTmst;
    //private static NotificationLogForm notLogForm;

    /**
     * Establishes the DB connection.
     * @return The connection
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Reads and returns notifications along with their details and usernames.
     * @return A list of notifications
     */
    public ArrayList<Notification> readNotifications(Timestamp minDate, Timestamp maxDate, Boolean initialLoadInd) {
        ArrayList<Notification> notifications = readNotificationBasics(minDate, maxDate, initialLoadInd);
        readUsernames(notifications);
        return notifications;
    }

    /**
     * Adds notifications and their details from the NOTIFICATION table to a list.
     * @return A list of notifications within the specified date range
     */
    private ArrayList<Notification> readNotificationBasics(Timestamp minDate, Timestamp maxDate, Boolean initialLoadInd) {
        if (initialLoadInd) {
            sqlStringName = LOAD_SQL;
        } else {
            sqlStringName = NOTIFICATION_SQL;
        }
        ArrayList<Notification> notifications = new ArrayList<>();
        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlStringName);

        ) {
            if (!initialLoadInd) {
                stmt.setTimestamp(1, minDate);
                stmt.setTimestamp(2, maxDate);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(rs.getInt("MessageID"),
                        rs.getTimestamp("DateTime"),
                        rs.getString("Message"),
                        rs.getInt("UserID"),
                        rs.getInt("RecipientCount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (initialLoadInd) {
            maxTmst = notifications.get(0).getDateTime();
            System.out.println("Initial max tmst: " + maxTmst);
            minTmst = notifications.get(notifications.size() - 1).getDateTime();
            System.out.println("Initial min tmst: " + minTmst);
        }
        return notifications;
    }

    /**
     * Reads and sets the usernames for the notifications that are returned from the query.
     */
    private void readUsernames(ArrayList<Notification> notifications) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(USER_SQL);
        ) {
            for (Notification not : notifications) {
                stmt.setInt(1, not.getUserId());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    not.setUsername(rs.getString("Username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getMaxTmst() {
        return maxTmst;
    }

    public Timestamp getMinTmst() {
        return minTmst;
    }

}
