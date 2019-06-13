package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
    private String subject = "A message from PCC Panther Pantry!";
    private String msgText;
    private final String username= "jjb.234a.test@gmail.com";
    private final String password = "xqaddkztgrcbdlda";  // App password for gmail, not actual password.
    private Session session;

    public MessageBuilder(Recipient recipient, String msgText) {
        this.recipient = recipient;
        this.sendTo = sentFrom;
        this.msgText = msgText;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        this.session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPassWordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
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
     */
    public void sendPlainMessage() {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sentFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setSubject(subject);
            message.setText(formatMessage( recipient, msgText));
            Transport.send(message, username, password);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the message has an Image tag, place the message body in the Multipart Message, add the image, and
     * send the message with the inline image included.
     * @param imagePath
     */
    public void sendMessageWithImage(String imagePath) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sentFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setSubject(subject);

            // Add Message Body.
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlMessageText = "<H1>Hello, " + recipient.getFirstName() + "</H1><br>" +
                    "<p>" + msgText + "</p>" +
                    "<br>Come on Down!\n" +
                    "PCC Foodbank Project\n" +
                    "jjb.234a.test@gmail.com\n" +
                    "(555)-867-5309";
            messageBodyPart.setContent(htmlMessageText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Add Image
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(imagePath);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            Transport.send(message, username, password);
        } catch(MessagingException e) {
            e.printStackTrace();
        }
    }
}
