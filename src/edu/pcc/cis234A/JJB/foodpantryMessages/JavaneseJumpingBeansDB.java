package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;
import java.util.ArrayList;

/**
 * The DB class for the notification log
 *
 * @author Liana Schweitzer
 * @version 2019.05.07
 *
 * Sprint 1 Modifications:
 * - Added a new field that contains the SQL statement for the initial table load.
 * - Added fields for setting which SQL statement will be used in the query.
 * - Refactored readNotifications and readNotificationBasics so that they could be used for the initial load query.
 *
 * Sprint 2 Modifications:
 * - Added new SQL statement strings to retrieve the logged in user's ID, their subscription settings, their user
 * settings, and to update each one of the columns of the USER_SETTING table.
 * - Added a method to set the logged in user's ID based on their username.
 * - Added a method to retrieve the user's subscription settings.
 * - Added a method to retrieve the user's settings.
 * - Added a method, for each column in the USER_SETTING table, to update the column.
 */
public class JavaneseJumpingBeansDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String LOAD_SQL = "SELECT TOP 200 * FROM NOTIFICATION ORDER BY DateTime DESC, MessageID DESC";
    private static final String NOTIFICATION_SQL = "SELECT * FROM NOTIFICATION " +
            "WHERE DateTime >= ? AND DateTime <= ? ORDER BY DateTime DESC, MessageID DESC";
    private static final String USERNAME_SQL = "SELECT Username FROM [USER] WHERE UserID = ?";
    private static final String USER_ID_SQL = "SELECT UserID FROM [USER] WHERE Username = ?";
    private static final String SUBSCRIPTION_SETTINGS_SQL = "SELECT * FROM USER_SETTING WHERE UserID = ?";
    private static final String USER_SETTINGS_SQL = "SELECT Email, AltEmail, Phone FROM [USER] WHERE UserID = ?";
    private static final String UPDATE_NOTIFICATIONS_ON_SQL = "UPDATE USER_SETTING SET NotificationsOn = ? WHERE UserID = ?";
    private static final String UPDATE_CASCADE_ON_SQL = "UPDATE USER_SETTING SET CascadeOn = ? WHERE UserID = ?";
    private static final String UPDATE_ROCK_CREEK_ON_SQL = "UPDATE USER_SETTING SET RockCreekOn = ? WHERE UserID = ?";
    private static final String UPDATE_SOUTHEAST_ON_SQL = "UPDATE USER_SETTING SET SoutheastOn = ? WHERE UserID = ?";
    private static final String UPDATE_SYLVANIA_ON_SQL = "UPDATE USER_SETTING SET SylvaniaOn = ? WHERE UserID = ?";
    private static final String UPDATE_EMAIL_ON_SQL = "UPDATE USER_SETTING SET EmailOn = ? WHERE UserID = ?";
    private static final String UPDATE_ALT_EMAIL_ON_SQL = "UPDATE USER_SETTING SET AltEmailOn = ? WHERE UserID = ?";
    private static final String UPDATE_SMS_ON_SQL = "UPDATE USER_SETTING SET SMSOn = ? WHERE UserID = ?";
    private static final String UPDATE_ALT_EMAIL_PHONE_USER_SETTINGS_SQL = "UPDATE [USER] SET AltEmail = ?, Phone = ? WHERE UserID = ?";
    private static final String UPDATE_ALT_EMAIL_USER_SETTINGS_SQL = "UPDATE [USER] SET AltEmail = ? WHERE UserID = ?";
    private static final String UPDATE_PHONE_USER_SETTINGS_SQL = "UPDATE [USER] SET Phone = ? WHERE UserID = ?";
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
                PreparedStatement stmt = conn.prepareStatement(USERNAME_SQL);
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
                PreparedStatement stmt = conn.prepareStatement(USER_ID_SQL);
        ) {
            stmt.setString(1, username);
            //System.out.println("PreparedStatement: " + stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userId = rs.getInt("UserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and returns subscription settings for the logged in user
     * @return The subscriptions settings
     */
    public SubscriptionSetting readSubscriptionSettings(String username) {
        SubscriptionSetting subscriptionSetting = new SubscriptionSetting();
        setUserId(username);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptionSetting;
    }

    /**
     * Reads and returns user settings for the logged in user
     * @return The user settings
     */
    public UserSetting readUserSettings() {
        UserSetting userSetting = new UserSetting();

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
            /*System.out.println("Email: " + userSetting.getEmail() +
                    " || Alt Email: " + userSetting.getAltEmail() +
                    " || Phone #: " + userSetting.getPhoneNbr());*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userSetting;
    }

    /**
     * Updates the NotificationsOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateNotificationsOn(boolean newNotficationsOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_NOTIFICATIONS_ON_SQL);
        ) {
            stmt.setBoolean(1, newNotficationsOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the CascadeOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateCascadeOn(boolean newCascadeOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_CASCADE_ON_SQL);
        ) {
            stmt.setBoolean(1, newCascadeOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the RockCreekOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateRockCreekOn(boolean newRockCreekOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_ROCK_CREEK_ON_SQL);
        ) {
            stmt.setBoolean(1, newRockCreekOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the SoutheastOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateSoutheastOn(boolean newSoutheastOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_SOUTHEAST_ON_SQL);
        ) {
            stmt.setBoolean(1, newSoutheastOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the SylvaniaOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateSylvaniaOn(boolean newSylvaniaOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_SYLVANIA_ON_SQL);
        ) {
            stmt.setBoolean(1, newSylvaniaOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the EmailOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateEmailOn(boolean newEmailOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_EMAIL_ON_SQL);
        ) {
            stmt.setBoolean(1, newEmailOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the AltEmailOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateAltEmailOn(boolean newAltEmailOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_ALT_EMAIL_ON_SQL);
        ) {
            stmt.setBoolean(1, newAltEmailOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the SMSOn column in the USER_SETTING table of the database based on the selection
     * of the logged in user.
     */
    public void updateSmsOn(boolean newSmsOn) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_SMS_ON_SQL);
        ) {
            stmt.setBoolean(1, newSmsOn);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the alternate email and phone number in the USER table of the database based on values provided by
     * the logged in user.
     */
    public void updateUserSettings(String altEmail, String phoneNbr) {
        String sql = "";
        int i = 0;
        if (!altEmail.isEmpty() && !phoneNbr.isEmpty()) {
            sql = UPDATE_ALT_EMAIL_PHONE_USER_SETTINGS_SQL;
            i = 1;
        } else if (!altEmail.isEmpty()) {
            sql = UPDATE_ALT_EMAIL_USER_SETTINGS_SQL;
            i = 2;
        } else if (!phoneNbr.isEmpty()) {
            sql = UPDATE_PHONE_USER_SETTINGS_SQL;
            i = 3;
        } else if (altEmail.isEmpty() && !phoneNbr.isEmpty()) {
            sql = UPDATE_ALT_EMAIL_USER_SETTINGS_SQL;
            i = 4;
        } else if (!altEmail.isEmpty() && phoneNbr.isEmpty()) {
            sql = UPDATE_PHONE_USER_SETTINGS_SQL;
            i = 5;
        } else if (altEmail.isEmpty() && phoneNbr.isEmpty()) {
            sql = UPDATE_ALT_EMAIL_PHONE_USER_SETTINGS_SQL;
            i = 6;
        }
        else {
            System.out.println("Entered else statement.  Returning ...");
            return;
        }
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            System.out.println("Update User Settings sql value: " + sql);
            System.out.println("Alt Email: " + altEmail);
            System.out.println("Phone #: " + phoneNbr);
            System.out.println("User ID: " + userId);
            if (i == 1) {
                System.out.println("Entered #1 if statement ...");
                stmt.setString(1, altEmail);
                stmt.setString(2, phoneNbr);
                stmt.setInt(3, userId);
            } else if (i == 2) {
                System.out.println("Entered #2 if statement ...");
                stmt.setString(1, altEmail);
                stmt.setInt(2, userId);
            } else if (i == 3) {
                System.out.println("Entered #3 if statement ...");
                stmt.setString(1, phoneNbr);
                stmt.setInt(2, userId);
            } else if (i == 4) {
                System.out.println("Entered #4 if statement ...");
                stmt.setNull(1, java.sql.Types.VARCHAR);
                stmt.setInt(2, userId);
            } else if (i == 5) {
                System.out.println("Entered #5 if statement ...");
                stmt.setNull(1, java.sql.Types.VARCHAR);
                stmt.setInt(2, userId);
            } else if (i == 6) {
                System.out.println("Entered #6 if statement ...");
                stmt.setNull(1, java.sql.Types.VARCHAR);
                stmt.setNull(2, java.sql.Types.VARCHAR);
                stmt.setInt(3, userId);
            }
            if (!sql.isEmpty()) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
