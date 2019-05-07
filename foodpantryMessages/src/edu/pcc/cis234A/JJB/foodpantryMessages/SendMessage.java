package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class SendMessage {


    private SendMessage() {}

    public static void main(String[] args) {
        String recipient = "william.wheatleyuhl@pcc.edu";
        String sentFrom = "shipwreckwill@gmail.com";
        final String username= "jjb.234a.test@gmail.com";
        final String password = "vK!18%sPP5*nssJE";
        String host = "smtp.gmail.com";
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
            message.setText("Test Message");
            Transport.send(message, username, password);
            System.out.println("Message Sent");
        } catch(MessagingException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

}
