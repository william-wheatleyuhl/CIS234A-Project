package edu.pcc.cis234A.JJB.foodpantryMessages;

/**
 * The UserSetting class
 *
 * @author Liana Schweitzer
 * @version 2019.06.03
 */
public class UserSetting {
    private String email;
    private String altEmail;
    private String phoneNbr;

    public UserSetting() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAltEmail() {
        return altEmail;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }
}
