package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;

/**
 * Created by vztr on 5/7/19
 */
public class Presentation {
    private JTabbedPane tabbedPane1;
    private JPanel rootPanel;
    private String username = "ZeroCool";
    private JLabel userLoggedInLabel;
    private JPanel sendNotificationTab;
    private JPanel templateTab;
    private JPanel msgLogTab;

    public Presentation() {
        userLoggedInLabel.setText("Logged in as " + username);
        sendNotificationTab.add(new NotificationForm().getRootPanel());
//        templateTab.add(new NotificationForm().getRootPanel());
//        msgLogTab.add(new createTemplateForm().getRootPanel());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
