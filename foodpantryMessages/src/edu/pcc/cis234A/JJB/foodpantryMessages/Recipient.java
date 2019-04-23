package edu.pcc.cis234A.JJB.foodpantryMessages;

public class Recipient {
    public String firstName;
    public String lastName;
    public String phoneNbr;
    public String emailAddr;

    public Recipient(String firstName, String lastName) {
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
