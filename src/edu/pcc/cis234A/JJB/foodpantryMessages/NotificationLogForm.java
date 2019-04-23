package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;

/**
 * @author Liana
 * @version 2019.04.21
 */
public class NotificationLogForm {
    private JPanel rootPanel;
    private JLabel pageNameLabel;
    private JLabel userLoggedInLabel;
    private JLabel messageNumberLabel;
    private JLabel timestampLabel;
    private JLabel sentByLabel;
    private JLabel recipientCountLabel;
    private JLabel messagePreviewLabel;
    private JTable notificationDataTable;
    private JScrollPane tableScrollPane;
    private JLabel minDateLabel;
    private JPanel minDatePanel;
    private JLabel maxDateLabel;
    private JPanel maxDatePanel;
    private JLabel fullMessageLabel;
    private JScrollPane fullMessageScrollPane;

    JDateChooser jMinDateChooser = new JDateChooser();
    JDateChooser jMaxDateChooser = new JDateChooser();

    public NotificationLogForm() {
        rootPanel.setPreferredSize(new Dimension(800, 600));

        minDatePanel.add(jMinDateChooser);
        maxDatePanel.add(jMaxDateChooser);
    }

    /**
     * Returns the root panel associated with the form
     * @return the root panel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
