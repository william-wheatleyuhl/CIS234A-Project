package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;

/**
 * @author Liana
 * @version 2019.04.21
 */
public class NotificationLogForm {
    private JPanel rootPanel;
    private JLabel pageName;
    private JLabel userLoggedInLabel;
    private JLabel dateLabel;
    private JLabel timeLabel;
    private JLabel sentByLabel;
    private JLabel sentToLabel;
    private JLabel messageLabel;
    private JTable notificationDataTable;
    private JScrollPane tableScrollPane;

    public NotificationLogForm() {
        rootPanel.setPreferredSize(new Dimension(800, 600));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
