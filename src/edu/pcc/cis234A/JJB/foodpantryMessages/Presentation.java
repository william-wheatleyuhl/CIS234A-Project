package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


/**
 * This class represents the implementation of the Presentation GUI. In this class, other modules are populated
 * into a JTabbedPane, and can be selected by using the tabs at the top of the screen.
 * @author William Wheatley-Uhl
 * @version 2019.05.06
 *
 * Changelog:
 * 20190610 WWU - Implemented logic to only allow managers to see All Tabs, Staff and Users have limited functionality
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
//        sendNotificationTab.add(nForm.getRootPanel());
//        templateTab.add(new CreateTemplateForm().getRootPanel());
//        manageRolesTab.add(new ManageUsersForm().getRootPanel());
//        msgLogTab.add(new NotificationLogForm().getRootPanel());
//        fpSettingsTab.add(new SettingsForm().getRootPanel());

        if(isUser()) {
            tabbedPane1.remove(sendNotificationTab);
            tabbedPane1.remove(templateTab);
            tabbedPane1.remove(manageRolesTab);
            tabbedPane1.remove(msgLogTab);
            fpSettingsTab.add(new SettingsForm().getRootPanel());
        }else if(isStaff()) {
            tabbedPane1.remove(templateTab);
            tabbedPane1.remove(manageRolesTab);
            sendNotificationTab.add(nForm.getRootPanel());
            msgLogTab.add(new NotificationLogForm().getRootPanel());
            fpSettingsTab.add(new SettingsForm().getRootPanel());
        } else {
            sendNotificationTab.add(nForm.getRootPanel());
            templateTab.add(new CreateTemplateForm().getRootPanel());
            manageRolesTab.add(new ManageUsersForm().getRootPanel());
            msgLogTab.add(new NotificationLogForm().getRootPanel());
            fpSettingsTab.add(new SettingsForm().getRootPanel());
        }
        tabbedPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if(tabbedPane1.getTabCount() > 1 && tabbedPane1.getSelectedIndex() == 0) {
                nForm.refreshLists();
//                    System.out.println("Yes");
                }
            }

        });
    }

    /**
     * Check to see if the user has a roleID representing a Staff member
     * @return True if the user has a roleID of 2
     */
    private boolean isStaff() {
        boolean isStaff = false;
        for(Recipient user : users) {
            if(user.getUserID() == userID && user.getRoleID() == 2) {
                isStaff = true;
            }
        }
        return isStaff;
    }

    /**
     * Check to see if the user has a roleID representing a Subscriber
     * @return True if the user has a roleID of 3
     */
    private boolean isUser() {
        boolean isUser = false;
        for(Recipient user : users) {
            if(user.getUserID() == userID && user.getRoleID() == 3) {
                isUser = true;
            }
        }
        return isUser;
    }
    
    /**
     * Sends the JPanel object to a requesting class.
     * @return A JPanel object representing the GUI of this class.
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
