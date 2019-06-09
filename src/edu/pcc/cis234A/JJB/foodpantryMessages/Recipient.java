package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.util.List;

/**
 *Recipient Class
 * This Class will model the recipient, and will store Recipient data.
 * @author Will Wheatley-Uhl
 * @version 2019.06.03
 */
public class Recipient {
    private int userID;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddr;
    private String phoneNbr;

    public Recipient(int userID, String userName, String lastName, String firstName, String emailAddr, String phoneNbr) {
        this.userID = userID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddr = emailAddr;
        this.phoneNbr = phoneNbr;
    }

    //    Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    //    Getters
    public int getUserID() {
        return userID;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

}
