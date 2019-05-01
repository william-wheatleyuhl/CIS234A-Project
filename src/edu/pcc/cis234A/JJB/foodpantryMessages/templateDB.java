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
}
