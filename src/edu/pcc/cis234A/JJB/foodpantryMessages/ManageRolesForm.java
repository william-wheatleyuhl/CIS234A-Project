package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * A class to define the functionality of the Manage Roles GUI
 * Allows a Manager to change user roles and edit or create
 * user groups (subscriber lists)
 *
 * @author Syn
 * @version 2019.06.08
 */
public class ManageRolesForm {
    private JPanel rootPanel;
    private JRadioButton radioEditExisting;
    private JRadioButton radioCreateNew;
    private JTextField fieldGroupName;
    private JLabel labelSelectUser;
    private JLabel labelSelectUsers;
    private JLabel labelGroupName;
    private JButton buttonSaveGroup;
    private JComboBox comboBoxRoles;
    private JComboBox comboBoxUsers;
    private JLabel labelSelectRole;
    private JLabel labelRole;
    private JButton buttonSubmitRole;
    private JCheckBox checkBoxConfirm;
    private JLabel labelCurrentRoleText;
    private JLabel labelCurrentRole;
    private JLabel labelManageGroups;
    private JLabel labelManageRoles;
    private JComboBox comboBoxGroups;
    private JScrollPane scrollPaneUsers;
    SubscriberDB subs = new SubscriberDB();
    private ArrayList<Recipient> recipients = subs.readSubscriberData();
    //private ArrayList<Role> roles = subs.readRoles();

    private int newUserRole;
    private int newUserID;

    DefaultComboBoxModel modelUsers = (DefaultComboBoxModel) comboBoxUsers.getModel();
    DefaultComboBoxModel modelRoles = (DefaultComboBoxModel) comboBoxRoles.getModel();
    DefaultComboBoxModel modelGroups = (DefaultComboBoxModel) comboBoxGroups.getModel();

    public ManageRolesForm() {
        populateUsers();
        populateRoles();
        comboBoxRoles.setEnabled(false);
        checkBoxConfirm.setEnabled(false);
        checkBoxConfirm.setVisible(false);
        comboBoxGroups.setEnabled(false);
        fieldGroupName.setEnabled(false);
        scrollPaneUsers.setEnabled(false);
        buttonSubmitRole.setEnabled(false);

        rootPanel.setPreferredSize(new Dimension(800, 600));

        /**
         * Item listener for the first combo box (Users)
         * Displays the selected user's role
         * Enables second combo box for role selection
         */
        comboBoxUsers.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBoxUsers.getModel().getSize() > 1) {
                    int selectedIndex = comboBoxUsers.getSelectedIndex();
                    if (selectedIndex == 0) {
                        comboBoxRoles.setEnabled(false);
                    } else {
                        comboBoxRoles.setEnabled(true);
                        //TODO: Populate with user's current role
                        labelCurrentRole.setText("Placeholder for user's current role");
                    }
                }
            }
        });

        /**
         * Item listener for the second combo box (Roles)
         * Displays a check box to confirm the changes to the user's role
         */
        comboBoxRoles.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBoxRoles.getModel().getSize() > 1) {
                    int selectedIndex = comboBoxRoles.getSelectedIndex();
                    if (selectedIndex > 0) {
                        buttonSubmitRole.setEnabled(true);
                        checkBoxConfirm.setVisible(true);
                        checkBoxConfirm.setEnabled(true);
                        checkBoxConfirm.setText("Check here to confirm you want to change this user's role");
                    }
                }
            }
        });

        /**
         * Listener for buttonSubmit
         * Submits the changes to the user's role to the DB
         * Checks to verify checkBoxConfirm is selected
         */
        buttonSubmitRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checkUserRole = false;
                boolean checkCheckBox = false;
                boolean checkUserSelected = false;

                //Check that a user is selected
                if(comboBoxUsers.getSelectedIndex() > 0) {
                    checkUserSelected = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a user to change role");
                }

                //Check that a role is selected for the user
                if(comboBoxRoles.getSelectedIndex() > 0) {
                    checkUserRole = true;
                    //TODO: Add user role variable
                    //newUserRole =
                } else {
                    JOptionPane.showMessageDialog(null, "You have not selected a role for the user");
                }

                //Check that the Check Box is selected
                if(checkBoxConfirm.isSelected()) {
                    checkCheckBox = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Please check the confirmation box to continue");
                }

                //Check that everything is true/selected before submitting to DB
                if(checkUserRole && checkCheckBox && checkUserSelected) {
                    JOptionPane.showMessageDialog(null, "User's role has been updated");
                    //TODO: Update DB with new user role (get the user ID & Role ID selected in combo boxes)
                    subs.updateUserRole(newUserRole, newUserID);
                }
            }
        });

        /**
         * Listener for radioCreateNew
         * Deselects radioEditExisting
         * Enables fieldGroupName and scrollPaneUsers
         */
        //TODO: Add listener for radio Create New

        /**
         * Listener for radioEditExisting
         * Deselects radioCreateNew
         * Enables comboBoxGroups
         */
        //TODO: Add listener for radio Edit Existing

        /**
         * Listener for comboBoxGroups selection
         * Enables fieldGroupName and scrollPaneUsers
         * Populates fieldGroupName with the group name from DB for selected index
         */
        //TODO: Add listener for comboBoxGroups selection

        /**
         * Listener for buttonSaveGroup
         * Checks that fieldGroupName has content
         * Does not require users to save a group
         * Adds users selected to the DB for that group
         */
        //TODO: Add listener for buttonSaveGroup
    }

    /**
     * Model for the first drop down menu to select existing users found in DB.
     */
    private void populateUsers() {
        modelUsers.removeAllElements();
        modelUsers.addElement("Choose one...");
        for(Recipient recipient : recipients) {
            modelUsers.addElement(recipient.getUserName());
        }
        comboBoxUsers.setModel(modelUsers);
    }

    /**
     * Model for the second drop down menu to select a role for the user.
     */
    private void populateRoles() {
        modelRoles.removeAllElements();
        modelRoles.addElement("Choose one...");
        //TODO: Change from manual entry to pull from DB
//        for(Role role : roles) {
//            modelRoles.addElement(roles.getRole());
//        }
        modelRoles.addElement("Subscriber");
        modelRoles.addElement("Staff");
        modelRoles.addElement("Manager");
        comboBoxRoles.setModel(modelRoles);
    }

    /**
     * Model for the third drop down menu to select existing groups found in DB.
     */
    private void populateGroups() {
        modelGroups.removeAllElements();
        modelGroups.addElement("Choose one...");
        //TODO: Add for loop to get groups
        comboBoxGroups.setModel(modelGroups);
    }

    private void populateSubscribers() {
        //this.
    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
