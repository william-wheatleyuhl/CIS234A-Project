package edu.pcc.cis234A.JJB.foodpantryMessages;
import java.sql.*;
import java.util.ArrayList;

/**
 * SubsciberDB Class
 * Connects to the Database, Returns requested data based on SQL Queries.
 * @author Will Wheatley-Uhl
 * @version 2019.04.22
 */
public class SubscriberDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String SUBSCRIBER_QUERY = "SELECT Username, LastName, FirstName, Email, RoleID FROM [USER]";
    private static final String TEMPLATE_QUERY = "SELECT TemplateID, TemplateName, MessageText FROM TEMPLATE";
    private static final String ID_QUERY = "SELECT MessageID FROM NOTIFICATION";
    private static final String LOG_MESSAGE = "INSERT INTO NOTIFICATION (MessageID, DateTime, Message, UserID, RecipientCount) VALUES(?,?,?,?,?)" ;
    private int subscriberCount = 0;


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Query the Database for enough data to populate the recipient Constructor. Add the newly created Recipient
     * objects to the recipients ArrayList.
     * @return recipients Return an ArrayList of Recipient Objects
     */
    public ArrayList readSubscriberData() {
        ArrayList<Recipient> receivers = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SUBSCRIBER_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            System.out.println("Reading Subscribers...");
            while (rs.next()) {
                receivers.add(new Recipient(rs.getString("Username"),
                        rs.getString("LastName"),
                        rs.getString("FirstName"),
                        rs.getString("Email"),
                        rs.getInt("RoleID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receivers;
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
     *
     * @param messageString
     */
    public void logMessage(String messageString) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOG_MESSAGE);
            Timestamp currTime = getSqlTime();
            stmt.setInt(1, getLastMessageID() + 1);
            stmt.setTimestamp(2, currTime);
            stmt.setString(3, messageString);
            stmt.setInt(4, 3);
            stmt.setInt(5, subscriberCount);
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
            System.out.println("Reading Messages...");
            while (rs.next()) {
                lastMessageID = rs.getInt("MessageID");
            }
            System.out.println("Last ID: "+ lastMessageID);
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
