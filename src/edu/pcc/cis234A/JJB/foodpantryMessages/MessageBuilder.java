package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.util.Properties;

/**
 * This class prepares the selected message to be sent to it's recipients. The message text and recipient object are passed
 * into the constructor. The message builder constructs the message using JavaMail Api and sends it.
 * @author William Wheatley-Uhl
 * @version 2019.05.25
 */
public class MessageBuilder {
    private Recipient recipient;
    private String sendTo;
    private String sentFrom = "jjb.234a.test@gmail.com";
    private String msgText;
    private final String username= "jjb.234a.test@gmail.com";
    private final String password = "xqaddkztgrcbdlda";  // App password for gmail, not actual password.

    public MessageBuilder(Recipient recipient, String msgText) {
        this.recipient = recipient;
        this.sendTo = recipient.getEmailAddr();
        this.msgText = msgText;
    }

    /**
     * Formats the message to have the recipient's first name, as well as a closing.
     * @param recipient
     * @param msgText
     * @return Formatted Message String with Tags filled, Greeting, and Closing.
     */
    public String formatMessage(Recipient recipient, String msgText) {
        String salutation = "Hello " + recipient.getFirstName() + ",\n\n";
        String closing = "\n\nCome on Down!\n" +
                "PCC Foodbank Project\n" +
                "jjb.234a.test@gmail.com\n" +
                "(555)-867-5309";
        return salutation + msgText + closing;
    }

    /**
     * Given all parameters of the message, send the message to the intended recipient.
     * TODO: Implement one send message usage by providing list of recipient email addresses, rather than calling this method multiple times.
     */
    public void sendMessage() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPassWordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sentFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setSubject("Test Message");
            message.setText(formatMessage( recipient, msgText));
            Transport.send(message, username, password);
            System.out.println("Message Sent to: " + sendTo);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }
}
