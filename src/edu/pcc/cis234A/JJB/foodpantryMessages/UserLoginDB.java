package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
/**
 * Author: Jeff Humphrey
 * Version: 2019.05.18
 */

public class UserLoginDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaneseJumpingBeans";
    private static final String USERNAME = "234a_JavaneseJumpingBeans";
    private static final String PASSWORD = "Nullifying9Defeating%";

    private static final String UserSelectStar = "SELECT * FROM [USER]";
    private static final String PasswordSelectStar = "SELECT * FROM [PASSWORD]";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    private ArrayList<String> emailList = new ArrayList<String>();

    /**
     * Finds the highest value UserID in the database and returns that number +1 for use with other functions where you must
     * insert a new UserID for a new user.
     */
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

    /**
     * Returns an ArrayList of Strings containing all the emails in the database. This is used in the VerifyEmail method of UserLogin class.
     */
    public ArrayList getUserEmails(){
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(UserSelectStar)
        ){
            emailList.clear();
            while(rs.next()){
                emailList.add(rs.getString("Email"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return emailList;
    }

    /**
     * This inserts a new user into the USER table of the database and their hashed password into the PASSWORD table.
     */
    public void insertUser(int highestUserID){
        try(
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO [USER] VALUES ('"+highestUserID+"', '"+UserLoginGUI.usernameDB[0]+"', '"+UserLoginGUI.lastNameDB+"', '"+UserLoginGUI.firstNameDB+"', '"+UserLoginGUI.emailDB+"', 3, 1, NULL, NULL)")
        ){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new users password and salt into the database.
     */
    public void insertUserPassword(int highestUserID){
        try(
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO [PASSWORD] VALUES ('"+UserLoginGUI.passwordDB+"', "+highestUserID+", '"+UserLogin.salt+"')");
                //PreparedStatement stmt2 = conn2.prepareStatement(insertPasswordString);
                //PreparedStatement stmt2 = conn2.prepareStatement("INSERT INTO [PASSWORD] VALUES ('testhash',35 , NULL)");
        ){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Initializes some default user settings for a new user.
     */
    public void insertDefaultSettings(int highestUserID){
        try(
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO [USER_SETTING] VALUES ("+highestUserID+", 1, 1, 1, 1, 1, 1, 0, 0)");
                //PreparedStatement stmt2 = conn2.prepareStatement(insertPasswordString);
                //PreparedStatement stmt2 = conn2.prepareStatement("INSERT INTO [PASSWORD] VALUES ('testhash',35 , NULL)");
        ){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Checks if a username exists in the database
     */
    public boolean checkUserExists(String username){
        boolean userExists = false;
        ArrayList<String> usernames = new ArrayList<String>();

        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(UserSelectStar)
        ){
            while(rs.next()){
                usernames.add(rs.getString("Username"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        for(String s : usernames){
            if(username.equals(s)){
                userExists = true;
            }
        }

        return userExists;
    }

    /**
     * Gets the UserID associated with a Username in the database. Only run this in the case that you run
     * CheckUserExists() and it returns True.
     */
    public int getUsernameUserID(String username){
        int userID = 0;
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(UserSelectStar)
        ){
            while(rs.next()){
                if(rs.getString("Username").equals(username)){
                    userID = rs.getInt("UserID");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    public int getUserIDRoleID(int userID){
        int roleID = 0;
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(UserSelectStar)
        ){
            while(rs.next()){
                if(rs.getInt("UserID") == userID){
                    roleID = rs.getInt("RoleID");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return roleID;
    }

    /**
     * Iterates through the PASSWORD table in the database until it finds a row with the UserID and then checks the associated hashed PW
     * and makes sure it's the same as the inputted one.
     */
    public boolean checkHashedPWUserMatch(String hashedPW, int userID){
        boolean pwMatch = false;
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(PasswordSelectStar)
        ){
            while(rs.next()){
                if(rs.getInt("UserID") == userID){
                    if(rs.getString("HashedPW").equals(hashedPW)){
                        pwMatch = true;
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return pwMatch;
    }

    /**
     * returns a salt associated with a user id from the db
     */
    public String getUserSalt(int userID){
        String userSalt = "";
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(PasswordSelectStar)
        ){
            while(rs.next()){
                if(rs.getInt("UserID") == userID){
                    userSalt = rs.getString("Pepper");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return userSalt;
    }

    public boolean getUserActive(int userID){
        boolean userActive = false;
        try(
                Connection conn = getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery(UserSelectStar)
        ){
            while(rs.next()){
                if(rs.getInt("UserID") == userID){
                    if(!rs.getBoolean("Active")){
                        userActive = false;
                    }
                    else{
                        userActive = true;
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return userActive;
    }

    public void deactivateUserAccount(int userID){
        try(
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE [USER] SET Active = 0 WHERE UserID = "+userID+";");
                //PreparedStatement stmt2 = conn2.prepareStatement(insertPasswordString);
                //PreparedStatement stmt2 = conn2.prepareStatement("INSERT INTO [PASSWORD] VALUES ('testhash',35 , NULL)");
        ){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void reactivateUserAccount(int userID){
        try(
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE [USER] SET Active = 1 WHERE UserID = "+userID+";");
                //PreparedStatement stmt2 = conn2.prepareStatement(insertPasswordString);
                //PreparedStatement stmt2 = conn2.prepareStatement("INSERT INTO [PASSWORD] VALUES ('testhash',35 , NULL)");
        ){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }



}
