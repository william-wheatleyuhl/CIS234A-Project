package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.util.Properties;

/**
 * @author William Wheatley-Uhl
 * @version 2019.05.06
 */
public class MessageBuilder {
    private Recipient recipient;
    private String sendTo;
    private String sentFrom = "jjb.234a.test@gmail.com";
    private String msgText;
    private final String username= "jjb.234a.test@gmail.com";
    private final String password = "xqaddkztgrcbdlda";  // App password for gmail, not actual password.

//    public MessageBuilder(String sendTo, String msgText) {
//        this.sendTo = sendTo;
//        this.msgText = msgText;
//    }

    public MessageBuilder(Recipient recipient, String msgText) {
        this.recipient = recipient;
        this.sendTo = recipient.getEmailAddr();
        this.msgText = msgText;
    }

    /**
     *
     * @param recipient
     * @param msgText
     * @return
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
     *
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
        JOptionPane.showMessageDialog(null, "Message Sent!");

    }
}
