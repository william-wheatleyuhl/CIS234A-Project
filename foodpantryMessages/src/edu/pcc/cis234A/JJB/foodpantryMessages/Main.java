package edu.pcc.cis234A.JJB.foodpantryMessages;
import javax.swing.*;

/**
 * Created by Will Wheatley-Uhl on 4/20/19
 */
public class Main {
    public static void createAndShowNotification() {
        JFrame frame = new JFrame("Food Pantry Notifications");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add( new NotificationForm().getRootPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowNotification());
    }

}
