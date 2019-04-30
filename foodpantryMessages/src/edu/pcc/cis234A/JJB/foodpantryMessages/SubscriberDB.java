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
    private static final String NAME_QUERY = "SELECT Username, LastName, FirstName FROM [USER]";
    private static final String TEMPLATE_QUERY = "SELECT TemplateID, TemplateName, MessageText FROM TEMPLATE";
    private static final String ID_QUERY = "SELECT MessageID FROM NOTIFICATION";
    private static final String LOG_MESSAGE = "INSERT INTO NOTIFICATION (MessageID, DateTime, Message, UserID, RecipientCount) VALUES(?,?,?,?,?)" ;



    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Query the Database for enough data to populate the recipient Constructor. Add the newly created Recipient
     * objects to the recipients ArrayList.
     * @return recipients Return an ArrayList of Recipient Objects
     */
    public ArrayList readSubscriberNames() {
        ArrayList<Recipient> receivers = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(NAME_QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            System.out.println("Reading Subscribers...");
            while (rs.next()) {
                receivers.add(new Recipient(rs.getString("Username"), rs.getString("LastName"), rs.getString("FirstName")));
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

    public void logMessage(String messageString, int subCount) {
        try {
                ResultSet rs = null;
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(LOG_MESSAGE);
                Timestamp currTime = getSqlTime();
                stmt.setInt(1, getLastMessageID() + 1);
                stmt.setTimestamp(2, currTime);
                stmt.setString(3, messageString);
                stmt.setInt(4, 3);
                stmt.setInt(5, subCount);
                stmt.executeQuery();
                rs = stmt.getGeneratedKeys();
                int key = rs.next() ? rs.getInt(1) : 0;
                if(key!=0){
                    System.out.println("Generated key="+key);
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLastMessageID() {
        int messageID = 0;
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(ID_QUERY);
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.println("Reading Messages...");
            while (rs.next()) {
                messageID = rs.getInt("MessageID");
            }
            System.out.println("Last ID: "+ messageID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageID;

    }

    public Timestamp getSqlTime() {
        Date sysDate = new Date(System.currentTimeMillis());
        Timestamp timeStamp = new Timestamp(sysDate.getTime());
        return timeStamp;
    }
}
