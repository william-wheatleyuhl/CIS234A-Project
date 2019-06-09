package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * SubsciberDB Class
 * Connects to the Database, Returns requested data based on SQL Queries.
 * @author Will Wheatley-Uhl
 * @version 2019.06.05
 */
public class SubscriberDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String SUBSCRIBER_QUERY = "SELECT UserID, Username, LastName, FirstName, Email, Phone FROM [USER]";
    private static final String SUB_SETTINGS_QUERY = "SELECT UserID, NotificationsOn, CascadeOn, RockCreekOn, SoutheastOn, SylvaniaOn, EmailOn, AltEmailOn, SMSOn FROM USER_SETTING";
    private static final String USER_GROUPS_UPDATE = "INSERT INTO USER_GROUP (UserID, GroupID) VALUES(?, ?)";
    private static final String USER_GROUPS_DELETE = "DELETE USER_GROUP WHERE UserID=? AND GroupID=?";
    private static final String USER_GROUP_QUERY = "SELECT UserID, USER_GROUP.GroupID, [GROUP].GroupName FROM USER_GROUP JOIN [GROUP] ON USER_GROUP.GroupID = [GROUP].GroupID;";
    private static final String TEMPLATE_QUERY = "SELECT TemplateID, TemplateName, MessageText FROM TEMPLATE";
    private static final String ID_QUERY = "SELECT MessageID FROM NOTIFICATION";
    private static final String LOG_MESSAGE = "INSERT INTO NOTIFICATION (MessageID, DateTime, Message, UserID, RecipientCount) VALUES(?,?,?,?,?)" ;
    private static final String LOG_RECIPIENTS = "INSERT INTO RECIPIENT (UserID, MessageID) VALUES(?,?)";
    private ArrayList<Recipient> receivers = new ArrayList<>();
    private HashMap<Integer, ArrayList<Object>> groups = new HashMap<>();
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Query the Database for enough data to populate the recipient Constructor. Add the newly created Recipient
     * objects to the recipients ArrayList.
     * @return recipients Return an ArrayList of Recipient Objects
     */
    public ArrayList readSubscriberData() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SUBSCRIBER_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            while (rs.next()) {
                receivers.add(new Recipient(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("LastName"),
                        rs.getString("FirstName"),
                        rs.getString("Email"),
                        rs.getString("Phone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        readSubscriberSettings();
        return receivers;
    }

    /**
     * Read subscriber settings from the USER_SETTING table, populate HashMap with individual user settings.
     */
    private void readSubscriberSettings() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SUB_SETTINGS_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            while (rs.next()) {
                for(Recipient recipient : receivers) {
                    if(recipient.getUserID() == rs.getInt("UserID")){
                        recipient.setUserSettings("NotificationsOn", rs.getBoolean("NotificationsOn"));
                        recipient.setUserSettings("SylvaniaOn", rs.getBoolean("SylvaniaOn"));
                        recipient.setUserSettings("CascadeOn", rs.getBoolean("CascadeOn"));
                        recipient.setUserSettings("RockCreekOn", rs.getBoolean("RockCreekOn"));
                        recipient.setUserSettings("SoutheastOn", rs.getBoolean("SoutheastOn"));
                        recipient.setUserSettings("EmailOn", rs.getBoolean("EmailOn"));
                        recipient.setUserSettings("AltEmailOn", rs.getBoolean("AltEmailOn"));
                        recipient.setUserSettings("SMSOn", rs.getBoolean("SMSOn"));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if USER_GROUP contains user/group membership listing
     * @param userID UserID to be checked.
     * @param groupID GroupID of Group to check membership of user against
     * @return Boolean value of whether the user is a member of the group or not.
     */
    private boolean checkUserGroup(int userID, int groupID) {
        boolean groupSet = false;
        if(groups.get(groupID).contains(userID)) {
            groupSet = true;
        }
        return groupSet;
    }

    /**
     * Check USER_SETTINGS against the listing in USER_GROUP. Add user's preferences if not in USER_GROUP, remove
     * them from USER_GROUP if they are set to false in USER_SETTINGS.
     * @param groupID The ID # of the group being checked against.
     * @param key The Setting text to be checked against.
     */
    private void checkSettingsAgainstGroups(int groupID, String key) {
        for(Recipient recipient : receivers) {
            HashMap<String, Boolean> settings = recipient.getUserSettings();
            if(settings.containsKey(key) && settings.get(key).equals(true)) {
                if(!checkUserGroup(recipient.getUserID(), groupID)) {
//                    Output for debugging
//                    System.out.println("Setting found, not in USER_GROUP. Setting in USER_GROUP");
                    addUserGroup(recipient.getUserID(), groupID);
                    groups.get(groupID).add(recipient.getUserID());
                }
            } else if(settings.containsKey(key) && settings.get(key).equals(false)) {
                if (checkUserGroup(recipient.getUserID(), groupID)) {
//                    Output for debugging
//                    System.out.println("Setting found, in USER_GROUP. not Setting in USER_GROUP");
                    deleteUserGroup(recipient.getUserID(), groupID);
                    groups.get(groupID).remove(recipient.getUserID());
                }
            }
        }
    }

    /**
     * Add specified listing from the USER_GROUPS table.
     * @param userID The integer UserID identifying the Recipient.
     * @param groupID The integer GroupID identifying the Group.
     */
    private void addUserGroup(int userID, int groupID) {
//        Output for debugging
//        System.out.println("Adding User Group Setting for " + userID);
        try {
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(USER_GROUPS_UPDATE);
                stmt.setInt(1, userID);
                stmt.setInt(2, groupID);
                stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove specified listing from the USER_GROUPS table.
     * @param userID The integer UserID identifying the Recipient.
     * @param groupID The integer GroupID identifying the Group.
     */
    private void deleteUserGroup(int userID, int groupID) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(USER_GROUPS_DELETE);
            stmt.setInt(1, userID);
            stmt.setInt(2, groupID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse rows in USER_GROUP. Get UserID's for each member of each group. Next, populate or remove rows of USER_GROUPS
     * based on the entries in USER_SETTINGS. If a setting is set to true, but does not exist in USER_GROUPS, add it. If
     * the setting is false but does exist in USER_GROUPS, remove it. Don't return group listings for distribution until
     * they've been populated and rechecked.
     * @return A HashMap containing GroupID numbers as Keys, and ArrayLists of UserID's who are members of that group.
     */
    public HashMap getGroupMakeup() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(USER_GROUP_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            while(rs.next()) {
                if(!groups.containsKey(rs.getInt("GroupID"))) {
                    groups.computeIfAbsent(rs.getInt("GroupID"), k -> new ArrayList<>()).add(rs.getString("GroupName"));
                    groups.get(rs.getInt("GroupID")).add(rs.getInt("UserID"));
                } else {
                    groups.get(rs.getInt("GroupID")).add(rs.getInt("UserID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Output for debugging
//        System.out.println("Checking User Settings against USER_GROUP table.");
        for(Integer groupID : new HashSet<Integer>(groups.keySet())) {
            switch(groupID) {
                case 1:
                    checkSettingsAgainstGroups(groupID, "SylvaniaOn");
                    break;

                case 2:
                    checkSettingsAgainstGroups(groupID, "CascadeOn");
                    break;

                case 3:
                    checkSettingsAgainstGroups(groupID, "RockCreekOn");
                    break;

                case 4:
                    checkSettingsAgainstGroups(groupID, "SoutheastOn");
                    break;
            }
        }
        return groups;
    }

    /**
     * Grabs Template data from the database, and uses it in a Template Constructor. New Templates are then added
     * to an ArrayList of Templates.
     * @return templates An ArrayList of Template objects
     */
    public ArrayList readTemplates() {
        ArrayList<Template> templates = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(TEMPLATE_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            while (rs.next()) {
                templates.add(new Template(rs.getInt("TemplateID"), rs.getString("TemplateName"), rs.getString("MessageText")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return templates;
    }

    /**
     * Log Messages to the Notification Table.
     * Saves the TimeStamp the message was sent at, the Message Contents, the number of
     * subscribers it was sent to, as well as
     * @param messageString
     */
    public void logMessage(String messageString, int subCount, int currentUserID) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOG_MESSAGE);
            Timestamp currTime = getSqlTime();
            stmt.setInt(1, getLastMessageID() + 1);
            stmt.setTimestamp(2, currTime);
            stmt.setString(3, messageString);
            stmt.setInt(4, currentUserID);
            stmt.setInt(5, subCount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the recipients to the RECIPIENT Link table.
     * Takes in userIDs of recipients receiving the message, and the last message ID in the DB.
     * Logs both for each UserID.
     * Check in DB with this Query:
     * SELECT NOTIFICATION.MessageID
     * ,DateTime
     * ,Message
     * ,RECIPIENT.UserID
     * ,[USER].RoleID
     * ,[USER].Username
     * FROM NOTIFICATION
     * JOIN RECIPIENT ON NOTIFICATION.MessageID = RECIPIENT.MessageID
     * JOIN [USER] ON RECIPIENT.UserID = [USER].UserID
     * ORDER BY MessageID;
     * @param userID Integer value of the Recipient's UserID number.
     */
    public void logRecipients(int userID) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOG_RECIPIENTS);
            stmt.setInt(1, userID);
            stmt.setInt(2, getLastMessageID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Integer value of the last MessageID PK from the NOTIFICATION. This value is used
     * to Increment the MessageID for new Logged Messages.
     * @return lastMessageID The Value of the last message ID logged in the Notification Log.
     */
    public int getLastMessageID() {
        int lastMessageID = 0;
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(ID_QUERY);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                lastMessageID = rs.getInt("MessageID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastMessageID;
    }

    /**
     * Get Local System Date/Time, return SQL formatted Timestamp
     * @return timeStamp A SQL formatted TimeStamp
     */
    public Timestamp getSqlTime() {
        Date sysDate = new Date(System.currentTimeMillis());
        Timestamp timeStamp = new Timestamp(sysDate.getTime());
        return timeStamp;
    }
}
