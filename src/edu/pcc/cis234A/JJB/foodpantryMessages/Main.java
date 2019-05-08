package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;

/**
 * Main Class
 * Loads and Displays NotificationForm
 * @author Will Wheatley-Uhl
 * @version 2019.05.06
 */
public class Main {
    public static void createAndShowNotification() {
        JFrame frame = new JFrame("Food Pantry Notifications");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add( new Presentation().getRootPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowNotification());
    }

}
