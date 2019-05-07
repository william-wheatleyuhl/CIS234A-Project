package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MessageBuilder {
    private String recipient;
    private String sentFrom = "jjb.234a.test@gmail.com";
    private String msgText;
    private final String username= "jjb.234a.test@gmail.com";
    private final String password = "xqaddkztgrcbdlda";  // App password for gmail, not actual password.

    public MessageBuilder(String recipient, String msgText) {
        this.recipient = recipient;
        this.msgText = msgText;

    }

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
                return new PasswordAuthentication("shipwreckwill@gmail.com", "pazrjknlqccqhklx");
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sentFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Test Message");
            message.setText(msgText);
            Transport.send(message, username, password);
            System.out.println("Message Sent to: " + recipient);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }
}
