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

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

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
}
