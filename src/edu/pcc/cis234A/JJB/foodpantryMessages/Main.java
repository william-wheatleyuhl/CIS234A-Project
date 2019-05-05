package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The Main class for Sprint 1
 *
 * @author Liana Schweitzer
 * @version 2019.04.20
 */
public class Main {
    private static Timestamp minDate;
    private static Timestamp maxDate;

    /**
     * Creates and displays the Food Pantry window.
     */
    private static void createAndShowLog() {
        JFrame frame = new JFrame("Food Pantry Notification Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new NotificationLogForm().getRootPanel());
        frame.pack();
        frame.setVisible(true);
        runQuery();
    }

    public static void main(String[] args) {
        System.out.println("Food Pantry Notifications:");

        SwingUtilities.invokeLater(Main::createAndShowLog);

    }

    public static void runQuery() {
        JavaneseJumpingBeansDB jjb = new JavaneseJumpingBeansDB();
        //jjb.setMinDate(Timestamp.valueOf("2000-01-01 00:00:00.0"));
        ArrayList<Notification> notifications = jjb.readNotifications(minDate, maxDate);
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
    }

    /**
     * Sets the min date, which will be used as the first parameter for NOTIFICATION_SQL.
     */
    public void setMinDate(Timestamp minDate) {
        this.minDate = minDate;
        System.out.println("Main Min Date: " + this.minDate);
    }

    /**
     * Sets the max date, which will be used as the second parameter for NOTIFICATION_SQL.
     */
    public void setMaxDate(Timestamp maxDate) {
        this.maxDate = maxDate;
        System.out.println("Main Max Date: " + this.maxDate);
    }
}