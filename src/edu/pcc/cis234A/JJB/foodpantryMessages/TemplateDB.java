package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;
import java.util.ArrayList;

/**
 * Connects to the 234a_JavaneseJumpingBeans database & returns requested data based on SQL queries
 *
 * @author Syn
 * @version 2019.06.04
 *
 * 20190521 SC - Updated UPDATE_TEMPLATE SQL to use UserID instead of RoleID
 * 20190604 SC - Added LAST_EDIT to query USER table
 * 20190604 SC - Added getLastEditUser() method
 * 20190604 SC - Added UserID to readTemplates(), logNewTemplate(), & updateExistingTemplate()
 */
public class TemplateDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    //private static final String NAME_QUERY = "SELECT Username, LastName, FirstName FROM [USER]";
    private static final String TEMPLATE_QUERY = "SELECT TemplateID, TemplateName, MessageText, UserID FROM TEMPLATE";
    private static final String ID_QUERY = "SELECT TemplateID FROM TEMPLATE";
    private static final String LOG_TEMPLATE = "INSERT INTO TEMPLATE VALUES(?,?,?,?)";
    private static final String UPDATE_TEMPLATE = "UPDATE TEMPLATE SET TemplateName = ?, MessageText = ?, UserID = ? WHERE TemplateID = ?";
    private static final String LAST_EDIT = "SELECT * FROM [USER]";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    /**
     * Get username of the user who last edited a template
     * @return lastEditUser The username based on UserID
     */
    public String getLastEditUser(int lastEditUserID) {
        String lastEditUser = "";
        try (
            Connection conn = getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(LAST_EDIT)
        ) {
            while(rs.next()) {
                if(rs.getInt("UserID") == (lastEditUserID)){
                    lastEditUser = rs.getString("Username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastEditUser;
    }

    /**
     * Get template data from DB.
     * @return templates An ArrayList of template objects
     */
    public ArrayList readTemplates() {
        ArrayList<Template> templates = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(TEMPLATE_QUERY);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                templates.add(new Template(rs.getInt("TemplateID"),
                        rs.getString("TemplateName"),
                        rs.getString("MessageText"),
                        rs.getInt("UserID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return templates;
    }

    /**
     * Returns the integer value of the last TemplateID from the TEMPLATE table.
     * @return lastTemplateID The value of the last TemplateID
     */
    public int getLastTemplateID() {
        int lastTemplateID = 0;
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(ID_QUERY);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                lastTemplateID = rs.getInt("TemplateID");
            }
            System.out.println("Last TemplateID: " + lastTemplateID); //For testing purposes only
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastTemplateID;
    }

    /**
     * Submit NEW template data to DB when "Save" button is clicked
     * @param newTemplateString The template text to be added to DB
     * @param newTemplateName The template name to be added to DB
     * @param userID The user currently logged in
     */
    public void logNewTemplate(String newTemplateName, String newTemplateString, int userID) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOG_TEMPLATE);
            stmt.setInt(1, getLastTemplateID() + 1);
            stmt.setString(2, newTemplateName);
            stmt.setString(3, newTemplateString);
            stmt.setInt(4, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an existing template in DB when "Save" button is clicked
     * @param existingTemplateString The template text to be changed / updated in DB
     * @param existingTemplateName The template name to be changed / updated in DB
     * @param userID The user currently logged in
     * @param existingTemplateID The TemplateID from the DB identifying which row to modify
     */
    public void updateExistingTemplate(String existingTemplateName, String existingTemplateString, int userID, int existingTemplateID) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TEMPLATE);
            stmt.setString(1, existingTemplateName);
            stmt.setString(2, existingTemplateString);
            stmt.setInt(3, userID);
            stmt.setInt(4, existingTemplateID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}