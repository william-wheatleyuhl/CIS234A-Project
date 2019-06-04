package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;
import java.util.ArrayList;

/**
 * The DB class for the notification log
 *
 * @author Liana Schweitzer
 * @version 2019.05.07
 *
 * Modifications:
 * - Added a new field that contains the SQL statement for the initial table load.
 * - Added fields for setting which SQL statement will be used in the query.
 * - Refactored readNotifications and readNotificationBasics so that they could be used for the initial load query.
 */
public class JavaneseJumpingBeansDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String LOAD_SQL = "SELECT TOP 200 * FROM NOTIFICATION ORDER BY DateTime DESC, MessageID DESC";
    private static final String NOTIFICATION_SQL = "SELECT * FROM NOTIFICATION " +
            "WHERE DateTime >= ? AND DateTime <= ? ORDER BY DateTime DESC, MessageID DESC";
    private static final String USER_SQL = "SELECT Username FROM [USER] WHERE UserID = ?";
    private static final String USERID_SQL = "SELECT UserID FROM [USER] WHERE Username = ?";
    private static final String SUBSCRIPTION_SETTINGS_SQL = "SELECT * FROM USER_SETTING WHERE UserID = ?";
    private static final String USER_SETTINGS_SQL = "SELECT Email, AltEmail, Phone FROM [USER] WHERE UserID = ?";
    private int userId;

    /**
     * Establishes the DB connection.
     * @return The connection
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Reads and returns notifications along with their details and usernames
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
        // String used to set which query will be run
		String sqlStringName;
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

        return notifications;
    }

    /**
     * Reads and sets the usernames for the notifications that are returned from the query
     */
    private void readUsernames(ArrayList<Notification> notifications) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(USER_SQL);
        ) {
            for (Notification not : notifications) {
                stmt.setInt(1, not.getUserId());
                stmt.getMetaData();
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
     * Reads and returns notifications along with their details and usernames
     * @return A list of notifications
     */
    public void setUserId(String username) {
        userId = -1;
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(USERID_SQL);
        ) {
            System.out.println("setUserId username: " + username);
            stmt.setString(1, username);
            System.out.println("PreparedStatement: " + stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userId = rs.getInt("UserID");
            }
            System.out.println("setUserId userId: " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and returns notifications along with their details and usernames
     * @return A list of notifications
     */
    public SubscriptionSetting readSubscriptionSettings(String username) {
        SubscriptionSetting subscriptionSetting = new SubscriptionSetting();
        System.out.println("readSettings username: " + username);
        setUserId(username);
        System.out.println("readingSettings userId: " + userId);

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SUBSCRIPTION_SETTINGS_SQL);
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subscriptionSetting.setUserId(rs.getInt("UserID"));
                subscriptionSetting.setNotificationsOn(rs.getBoolean("NotificationsOn"));
                subscriptionSetting.setCascadeOn(rs.getBoolean("CascadeOn"));
                subscriptionSetting.setRockCreekOn(rs.getBoolean("RockCreekOn"));
                subscriptionSetting.setSoutheastOn(rs.getBoolean("SoutheastOn"));
                subscriptionSetting.setSylvaniaOn(rs.getBoolean("SylvaniaOn"));
                subscriptionSetting.setEmailOn(rs.getBoolean("EmailOn"));
                subscriptionSetting.setAltEmailOn(rs.getBoolean("AltEmailOn"));
                subscriptionSetting.setSmsOn(rs.getBoolean("SMSOn"));
            }
            System.out.println("UserID: " + subscriptionSetting.getUserId() +
                    " || NotificationsOn: " + subscriptionSetting.isNotificationsOn() +
                    " || CascadeOn: " + subscriptionSetting.isCascadeOn() +
                    " || RockCreekOn: " + subscriptionSetting.isRockCreekOn() +
                    " || SoutheastOn: " + subscriptionSetting.isSoutheastOn() +
                    " || SylvaniaOn: " + subscriptionSetting.isSylvaniaOn() +
                    " || EmailOn: " + subscriptionSetting.isEmailOn() +
                    " || AltEmailOn: " + subscriptionSetting.isAltEmailOn() +
                    " || SMSOn: " + subscriptionSetting.isSmsOn());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptionSetting;
    }

    /**
     * Reads and returns notifications along with their details and usernames
     * @return A list of notifications
     */
    public UserSetting readUserSettings() {
        UserSetting userSetting = new UserSetting();
        System.out.println("readUserSettings userId: " + userId);

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(USER_SETTINGS_SQL);
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userSetting.setEmail(rs.getString("Email"));
                if (rs.getString("AltEmail") == null) {
                    userSetting.setAltEmail("");
                } else {
                    userSetting.setAltEmail(rs.getString("AltEmail"));
                }
                if (rs.getString("Phone") == null) {
                    userSetting.setPhoneNbr("");
                } else {
                    userSetting.setPhoneNbr(rs.getString("Phone"));
                }
            }
            System.out.println("Email: " + userSetting.getEmail() +
                    " || Alt Email: " + userSetting.getAltEmail() +
                    " || Phone #: " + userSetting.getPhoneNbr());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userSetting;
    }

}
