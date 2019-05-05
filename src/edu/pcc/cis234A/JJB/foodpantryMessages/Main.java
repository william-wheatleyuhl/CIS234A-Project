package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;

/**
 * The Main class for Sprint 1
 *
 * @author Liana Schweitzer
 * @version 2019.04.20
 */
public class Main {

    private static NotificationLogForm notificationLogForm = new NotificationLogForm();

    /**
     * Creates and displays the Food Pantry window.
     */
    private static void createAndShowLog() {
        JFrame frame = new JFrame("Food Pantry Notification Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new NotificationLogForm().getRootPanel());
        frame.pack();
        frame.setVisible(true);
        notificationLogForm.runQuery();
    }

    public static void main(String[] args) {
        System.out.println("Food Pantry Notifications:");
        SwingUtilities.invokeLater(Main::createAndShowLog);
    }

}