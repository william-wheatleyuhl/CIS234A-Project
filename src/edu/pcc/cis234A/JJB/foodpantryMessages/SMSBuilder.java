package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSBuilder {
    private static final String ACCOUNT_SID = "ACfc3b13b0c449c2c201af3dfe2f226d76";
    private static final String AUTH_TOKEN = "77a3eac90207fac50ad388a07ac7cef7";
    private String msgToSend;

    public SMSBuilder(String msg) {
        this.msgToSend = msg;
    }

    public void sendSMS() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber("+13607138917"),
                new PhoneNumber("+19712568089"),
                msgToSend).create();
    }
}
