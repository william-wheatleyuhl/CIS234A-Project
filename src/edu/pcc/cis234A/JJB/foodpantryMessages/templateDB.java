package edu.pcc.cis234A.JJB.foodpantryMessages;

//import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import javax.xml.transform.Templates;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Connects to the 234a_JavaneseJumpingBeans database & returns requested data based on SQL queries
 *
 * @author Syn
 * @version 2019.04.29
 */
public class templateDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String NAME_QUERY = "SELECT Username, LastName, FirstName FROM [USER]";
    private static final String TEMPLATE_QUERY = "SELECT TemplateID, TemplateName, MessageText FROM TEMPLATE";
    private static final String ID_QUERY = "SELECT TemplateID FROM TEMPLATE";
    private static final String LOG_TEMPLATE = "INSERT INTO TEMPLATE VALUES(?,?,?,?)";
    private static final String UPDATE_TEMPLATE = "UPDATE TEMPLATE SET TemplateName = ?, MessageText = ?, RoleID = ? WHERE TemplateID = ?";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
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
                        rs.getString("MessageText")));
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
     * @param newTemplateString
     * @param newTemplateName
     */
    public void logNewTemplate(String newTemplateName, String newTemplateString) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(LOG_TEMPLATE);
            stmt.setInt(1, getLastTemplateID() + 1);
            stmt.setString(2, newTemplateName);
            stmt.setString(3, newTemplateString);
            //TODO: Change RoleID to UserID in DB for TEMPLATE table
            //TODO: Use UserID from session for below stmt
            stmt.setInt(4, 1);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an existing template in DB when "Save" button is clicked
     * @param existingTemplateString
     * @param existingTemplateName
     */
    public void updateExistingTemplate(String existingTemplateName, String existingTemplateString, int existingTemplateID) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TEMPLATE);
            stmt.setString(1, existingTemplateName);
            stmt.setString(2, existingTemplateString);
            //TODO: Change RoleID to UserID in DB for TEMPLATE table
            //TODO: Use UserID from session for below stmt
            stmt.setInt(3, 1);
            stmt.setInt(4, existingTemplateID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
