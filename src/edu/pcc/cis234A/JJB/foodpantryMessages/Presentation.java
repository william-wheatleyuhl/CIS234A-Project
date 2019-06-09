package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * This class represents the implementation of the Presentation GUI. In this class, other modules are populated
 * into a JTabbedPane, and can be selected by using the tabs at the top of the screen.
 * @author William Wheatley-Uhl
 * @version 2019.05.06
 *
 */
public class Presentation {
    private JTabbedPane tabbedPane1;
    private JPanel rootPanel;
    private UserLogin ul =  new UserLogin();
    private UserLoginDB uldb = new UserLoginDB();
    private String username = ul.getLoggedInUser();
    private int userID = uldb.getUsernameUserID(username);
    private int roleID = uldb.getUserIDRoleID(userID);
    private JLabel userLoggedInLabel;
    private JPanel sendNotificationTab;
    private JPanel templateTab;
    private JPanel msgLogTab;
    private JPanel manageRolesTab;
    private JPanel fpSettingsTab;

    public Presentation() {
        userLoggedInLabel.setText("Logged in as " + username);
        sendNotificationTab.add(new NotificationForm(username).getRootPanel());
        templateTab.add(new CreateTemplateForm().getRootPanel());
        msgLogTab.add(new NotificationLogForm().getRootPanel());
        manageRolesTab.add(new ManageRolesForm().getRootPanel());
        fpSettingsTab.add(new SettingsForm().getRootPanel());
    }

    /**
     * Sends the JPanel object to a requesting class.
     * @return A JPanel object representing the GUI of this class.
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
