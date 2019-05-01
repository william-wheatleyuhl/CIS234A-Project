package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The Main class for Sprint 1
 *
 * @author Liana Schweitzer
 * @version 2019.04.20
 */
public class Main {

    /**
     * Creates and displays the Food Pantry window.
     */
    private static void createAndShowLog() {
        JFrame frame = new JFrame("Food Pantry Notification Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new NotificationLogForm().getRootPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("Food Pantry Notifications:");
        JavaneseJumpingBeansDB jjb = new JavaneseJumpingBeansDB();
        ArrayList<Notification> notifications = jjb.readNotifications();
        int i = 1;

        for (Notification not: notifications) {
            String s = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(not.getDateTime());
            System.out.println("#: " + i +
                    " || Timestamp: " + s +
                    " || Sent By: " + not.getUsername() +
                    " || Recipient Count: " + not.getRecipientCount() +
                    " || Message: " + not.getMessage());
            i++;
        }
        SwingUtilities.invokeLater(Main::createAndShowLog);
    }
}