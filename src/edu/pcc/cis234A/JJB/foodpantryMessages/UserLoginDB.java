package pcc.cis234A.JJB.foodpantryMessages;

import java.sql.*;

public class UserLoginDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";

    private static final String UserSelectStar = "SELECT * FROM [USER]";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public int getHighestUserID(){
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(UserSelectStar)
                ){
            rs.last();
            return rs.getInt("UserID") + 1;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insertUser(int highestUserID){
        try(
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO [USER] VALUES ('"+highestUserID+"', '"+UserLoginGUI.usernameDB+"', '"+UserLoginGUI.lastNameDB+"', '"+UserLoginGUI.firstNameDB+"', '"+UserLoginGUI.emailDB+"', 3)")
        ){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
