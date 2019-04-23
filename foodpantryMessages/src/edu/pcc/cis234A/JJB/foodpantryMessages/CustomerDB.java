package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerDB {
    private static final String DB_URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
}
