package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;

public class SubscriberDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";
    private static final String QUERY = "SELECT Message FROM NOTIFICATION";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public void readSubscribers() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(QUERY);
                ResultSet rs = stmt.executeQuery()
                ) {
            System.out.println("Reading Subscribers...");
            while (rs.next()) {
                String msg = rs.getString("Message");
                System.out.println(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
