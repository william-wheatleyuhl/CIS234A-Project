package edu.pcc.cis234A.JJB.foodpantryMessages;

import java.sql.Timestamp;

/**
 * The Notification table class
 *
 * @author Liana Schweitzer
 * @version 2019.04.21
 */
public class Notification {
    private int messageId;
    private Timestamp dateTime;
    private String message;
    private int userId;
    private int recipientCount;
    private String username;

    public Notification(int messageId, Timestamp dateTime, String message, int userId, int recipientCount) {
        this.messageId = messageId;
        this.dateTime = dateTime;
        this.message = message;
        this.userId = userId;
        this.recipientCount = recipientCount;
        username = "";
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecipientCount() {
        return recipientCount;
    }

    public void setRecipientCount(int recipientCount) {
        this.recipientCount = recipientCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
