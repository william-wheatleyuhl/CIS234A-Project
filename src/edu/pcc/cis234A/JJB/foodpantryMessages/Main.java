package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;

/**
 * @author Liana
 * @version 2019.04.20
 */
public class Main {
    public static void createAndShowLog() {
        JFrame frame = new JFrame("Food Pantry Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new NotificationLogForm().getRootPanel());
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowLog());
    }
}