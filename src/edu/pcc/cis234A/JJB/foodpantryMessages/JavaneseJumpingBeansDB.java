package edu.pcc.cis234A.JJB.foodpantryMessages;

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
    private static final String NOTIFICATION_SQL = "SELECT * FROM NOTIFICATION " +
            "WHERE DateTime >= '2019-04-15' AND DateTime <= '2019-04-18' ORDER BY DateTime DESC";
    private static final String USER_SQL = "SELECT Username FROM [USER] WHERE UserID = ?";
    private String minDate;
    private String maxDate;

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
    public ArrayList<Notification> readNotifications() {
        ArrayList<Notification> notifications = readNotificationBasics();
        readUsernames(notifications);
        return notifications;
    }

    /**
     * Adds notifications and their details from the NOTIFICATION table to a list.
     * @return A list of notifications within the specified date range
     */
    private ArrayList<Notification> readNotificationBasics() {
        ArrayList<Notification> notifications = new ArrayList<>();
        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(NOTIFICATION_SQL);
            // stmt.setInt(1, not.getUserId());
            ResultSet rs = stmt.executeQuery();
        ) {
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

    /**
     * Sets the min date, which will be used as the first parameter for NOTIFICATION_SQL.
     */
    public void setMinDate(String minDate) {
        this.minDate = minDate;
        System.out.println("JJBDB Min Date: " + this.minDate);
    }

    /**
     * Sets the max date, which will be used as the second parameter for NOTIFICATION_SQL.
     */
    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }
}
