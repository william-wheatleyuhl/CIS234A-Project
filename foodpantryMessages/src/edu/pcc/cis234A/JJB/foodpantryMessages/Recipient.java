package edu.pcc.cis234A.JJB.foodpantryMessages;

/**
 *Recipient Class
 * This Class will model the recipient, and will store Recipient data.
 * @author Will Wheatley-Uhl
 * @version 2019.04.23
 */
public class Recipient {
    public String userName;
    public String firstName;
    public String lastName;
    public String phoneNbr;
    public String emailAddr;

    public Recipient(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

//    Setters

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    //    Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getPhoneNbr() {
        return phoneNbr;
    }

    public String getEmailAddr() {
        return emailAddr;
    }
}
