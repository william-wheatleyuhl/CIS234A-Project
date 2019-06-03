package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SubsciberDB Class
 * Connects to the Database, Returns requested data based on SQL Queries.
 * @author Will Wheatley-Uhl
 * @version 2019.05.25
 */
public class SubscriberDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String SUBSCRIBER_QUERY = "SELECT UserID, Username, LastName, FirstName, Email, Phone FROM [USER]";
    private static final String GROUPS_QUERY = "SELECT UserID, GroupID FROM USERGROUP";
    private static final String TEMPLATE_QUERY = "SELECT TemplateID, TemplateName, MessageText FROM TEMPLATE";
    private static final String ID_QUERY = "SELECT MessageID FROM NOTIFICATION";
    private static final String LOG_MESSAGE = "INSERT INTO NOTIFICATION (MessageID, DateTime, Message, UserID, RecipientCount) VALUES(?,?,?,?,?)" ;
    private static final String LOG_RECIPIENTS = "INSERT INTO RECIPIENT (UserID, MessageID) VALUES(?,?)";
    private ArrayList<Recipient> receivers = new ArrayList<>();
    private HashMap<Integer, ArrayList<Integer>> groups = new HashMap<>();
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
        return receivers;
    }

    public HashMap getGroupMakeup() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(GROUPS_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            while(rs.next()) {
                if(!groups.containsKey(rs.getInt("GroupID"))) {
                    groups.computeIfAbsent(rs.getInt("GroupID"), k -> new ArrayList<>()).add(rs.getInt("UserID"));
//                    groups.put(rs.getInt("GroupID"), List<Integer> members = new List<Integer>());
                } else {
                    groups.get(rs.getInt("GroupIO")).add(rs.getInt("UserID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
