package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Using the Twilio API, and a free trial account, build and send SMS messages to users with the appropriate settings.
 * @author William Wheatley-Uhl
 * @version 2019.06.10
 */

public class SMSBuilder {
    private static final String ACCOUNT_SID = "ACfc3b13b0c449c2c201af3dfe2f226d76";
    private static final String AUTH_TOKEN = "77a3eac90207fac50ad388a07ac7cef7";
    private String msgToSend;
    private Recipient recipient;

    public SMSBuilder(String msg, Recipient recipient) {
        this.msgToSend = msg;
        this.recipient = recipient;
    }

    /**
     * Build an SMS message with the recipient, the Twilio generated Phone Number,and the tag-parsed message
     */
    public void sendSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(formatPhoneNbr()),
                new PhoneNumber("+19712568089"),
                msgToSend).create();
    }

    /**
     * Covert the Subscriber's Telephone number to the Expected format.
     * @return String value of the Appropriately formatted Number.
     */
    private String formatPhoneNbr() {
        String formattedNbr = "+1";
        return formattedNbr + recipient.getPhoneNbr();
    }
}
