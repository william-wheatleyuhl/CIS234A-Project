package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;


/**
 * This class represents the implementation of the Presentation GUI. In this class, other modules are poulated
 * into a JTabbedPane, and can be selected by using the tabs at the top of the screen.
 * @author William Wheatley-Uhl
 * @version 2019.05.06
 *
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

    /**
     * Sends the JPanel object to a requesting class.
     * @return A JPanel object representing the GUI of this class.
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
