package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


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
    private NotificationForm nForm = new NotificationForm(username);
    private JLabel userLoggedInLabel;
    private JPanel sendNotificationTab;
    private JPanel templateTab;
    private JPanel msgLogTab;
    private JPanel manageRolesTab;
    private JPanel fpSettingsTab;
    private SubscriberDB subs = new SubscriberDB();
    ArrayList<Recipient> users = subs.readSubscriberData();

    public Presentation() {
        userLoggedInLabel.setText("Logged in as " + username);
        sendNotificationTab.add(nForm.getRootPanel());
        templateTab.add(new CreateTemplateForm().getRootPanel());
        manageRolesTab.add(new ManageRolesForm().getRootPanel());
        msgLogTab.add(new NotificationLogForm().getRootPanel());
        fpSettingsTab.add(new SettingsForm().getRootPanel());

        if(!checkManager()) {
            tabbedPane1.remove(templateTab);
            tabbedPane1.remove(manageRolesTab);

        }
        tabbedPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                nForm.refreshTemplates();
                nForm.populateTemplateMenu();
            }
        });
    }

    private boolean checkManager() {
        boolean isManager = false;
        for(Recipient user : users) {
            if(user.getUserID() == userID && user.getRoleID() == 1) {
                isManager = true;
            }
        }
        return isManager;
    }
    /**
     * Sends the JPanel object to a requesting class.
     * @return A JPanel object representing the GUI of this class.
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
