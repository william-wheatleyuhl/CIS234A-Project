package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.Timestamp;

/**
 * The Notification table class
 *
 * @author Liana Schweitzer
 * @version 2019.04.21
 */
public class SubscriptionSetting {
    private int userId;
    private boolean notificationsOn;
    private boolean cascadeOn;
    private boolean rockCreekOn;
    private boolean southeastOn;
    private boolean sylvaniaOn;
    private boolean emailOn;
    private boolean altEmailOn;
    private boolean smsOn;

    public SubscriptionSetting() {
        /*
        int userId, boolean notificationsOn, boolean cascadeOn, boolean rockCreekOn, boolean southeastOn, boolean sylvaniaOn, boolean emailOn, boolean altEmailOn, boolean smsOn
        this.userId = userId;
        this.notificationsOn = notificationsOn;
        this.cascadeOn = cascadeOn;
        this.rockCreekOn = rockCreekOn;
        this.southeastOn = southeastOn;
        this.sylvaniaOn = sylvaniaOn;
        this.emailOn = emailOn;
        this.altEmailOn = altEmailOn;
        this.smsOn = smsOn;*/
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isNotificationsOn() {
        return notificationsOn;
    }

    public void setNotificationsOn(boolean notificationsOn) {
        this.notificationsOn = notificationsOn;
    }

    public boolean isCascadeOn() {
        return cascadeOn;
    }

    public void setCascadeOn(boolean cascadeOn) {
        this.cascadeOn = cascadeOn;
    }

    public boolean isRockCreekOn() {
        return rockCreekOn;
    }

    public void setRockCreekOn(boolean rockCreekOn) {
        this.rockCreekOn = rockCreekOn;
    }

    public boolean isSoutheastOn() {
        return southeastOn;
    }

    public void setSoutheastOn(boolean southeastOn) {
        this.southeastOn = southeastOn;
    }

    public boolean isSylvaniaOn() {
        return sylvaniaOn;
    }

    public void setSylvaniaOn(boolean sylvaniaOn) {
        this.sylvaniaOn = sylvaniaOn;
    }

    public boolean isEmailOn() {
        return emailOn;
    }

    public void setEmailOn(boolean emailOn) {
        this.emailOn = emailOn;
    }

    public boolean isAltEmailOn() {
        return altEmailOn;
    }

    public void setAltEmailOn(boolean altEmailOn) {
        this.altEmailOn = altEmailOn;
    }

    public boolean isSmsOn() {
        return smsOn;
    }

    public void setSmsOn(boolean smsOn) {
        this.smsOn = smsOn;
    }
}
