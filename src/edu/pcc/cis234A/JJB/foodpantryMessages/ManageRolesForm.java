package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A class to define the functionality of the Manage Roles GUI
 * Allows a Manager to change user roles and edit or create
 * user groups (subscriber lists)
 *
 * @author Syn Calvo & William Wheatley-Uhl
 * @version 2019.06.08
 *
 * Changelog:
 * 20190609 WWU - Implemented working roles class
 * 20190609 WWU - Populate "Current Role" label dynamically
 * 20190609 WWU - Finished Submit button for changing subscriber roles.
 * 20190609 WWU - comboBoxGroups now populates, as does the fieldGroupName textField
 * 20190609 SC - Added functionality to save groups to DB
 * 20190609 SC - Cleaned up completed TODOs & added new TODOs
 * 20190610 SC - Added Refresh & Reset methods to activate on save / submit and refresh combo boxes
 * 20190610 SC - Added ghostUserID to use in USER_GROUP add on Save
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
    private JLabel labelGroupDesc;
    private JTextField fieldGroupDesc;
    private JList listUsers;
    SubscriberDB subs = new SubscriberDB();
    private ArrayList<Recipient> recipients = subs.readSubscriberData();
    private ArrayList<Role> roles = subs.readRoles();
    private HashMap<Integer, ArrayList<Object>> groups = subs.getGroupMakeup();
    private int currentRoleID;

    private int roleID;
    private int userID;
    private String newGroupName;
    private String newGroupDesc;
    private int existingGroupID;
    private int ghostUserID = 45;

    DefaultComboBoxModel modelUsers = (DefaultComboBoxModel) comboBoxUsers.getModel();
    DefaultComboBoxModel modelRoles = (DefaultComboBoxModel) comboBoxRoles.getModel();
    DefaultComboBoxModel modelGroups = (DefaultComboBoxModel) comboBoxGroups.getModel();

    public ManageRolesForm() {
        populateUsers();
        populateRoles();
        populateGroups();
        populateSubscribers();
        comboBoxRoles.setEnabled(false);
        checkBoxConfirm.setEnabled(false);
        //checkBoxConfirm.setVisible(false);
        comboBoxGroups.setEnabled(false);
        fieldGroupName.setEnabled(false);
        scrollPaneUsers.setEnabled(false);
        listUsers.setEnabled(false);
        buttonSubmitRole.setEnabled(false);
        fieldGroupDesc.setEnabled(false);
        labelCurrentRole.setText("None");

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
                        labelCurrentRole.setText("None");
                        checkBoxConfirm.setSelected(false);
                        comboBoxRoles.setSelectedIndex(0);
                    } else {
                        comboBoxRoles.setEnabled(true);
                        String currentRole = "";
                        for(Recipient recipient : recipients) {
                            if(recipient.getUserID() == selectedIndex) {
                                currentRole = recipient.getRoleTitle();
                            }
                        }
                        labelCurrentRole.setText(currentRole);
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
                        //checkBoxConfirm.setVisible(true);
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
                boolean checkRoles = false;
                currentRoleID = subs.getRoleNameRoleID(labelCurrentRole.getText());

                //Check that a user is selected
                if(comboBoxUsers.getSelectedIndex() > 0) {
                    checkUserSelected = true;
                    userID = comboBoxUsers.getSelectedIndex();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a user to change role");
                }

                //Check that a role is selected for the user
                if(comboBoxRoles.getSelectedIndex() > 0) {
                    checkUserRole = true;
                    roleID = comboBoxRoles.getSelectedIndex();
                    //System.out.println(roleID); testing
                } else {

                    JOptionPane.showMessageDialog(null, "You have not selected a role for the user");
                }

                //Check that the Check Box is selected
                if(checkBoxConfirm.isSelected()) {
                    checkCheckBox = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Please check the confirmation box to continue");
                }

                //Check that the user's role is not the same as the role selected
                if(currentRoleID == roleID) {
                    JOptionPane.showMessageDialog(null, "Current role and selected role cannot be the same");
                } else {
                    checkRoles = true;
                }

                //Check that everything is true/selected before submitting to DB
                if(checkUserRole && checkCheckBox && checkUserSelected && checkRoles) {
                    JOptionPane.showMessageDialog(null, "User's role has been updated");
                    subs.updateUserRole(roleID, userID);
                    resetManageUserRoles();
                }
            }
        });

        /**
         * Listener for radioCreateNew
         * Deselects radioEditExisting
         * Enables fieldGroupName and scrollPaneUsers
         */
        radioCreateNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(radioCreateNew.isSelected()) {
                    radioEditExisting.setSelected(false);
                    fieldGroupName.setEnabled(true);
                    fieldGroupName.setText("");
                    scrollPaneUsers.setEnabled(true);
                    comboBoxGroups.setEnabled(false);
                    listUsers.setEnabled(true);
                    fieldGroupDesc.setEnabled(true);
                    fieldGroupDesc.setText("");
                    comboBoxGroups.setSelectedIndex(0);
                }
            }
        });

        /**
         * Listener for radioEditExisting
         * Deselects radioCreateNew
         * Enables comboBoxGroups
         */
        radioEditExisting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(radioEditExisting.isSelected()) {
                    radioCreateNew.setSelected(false);
                    comboBoxGroups.setEnabled(true);
                    fieldGroupName.setEnabled(true);
                    scrollPaneUsers.setEnabled(true);
                    listUsers.setEnabled(true);
                    fieldGroupDesc.setEnabled(true);
                }
            }
        });

        /**
         * Listener for comboBoxGroups selection
         * Enables fieldGroupName and scrollPaneUsers
         * Populates fieldGroupName with the group name from DB for selected index
         */
        //TODO: FUTURE FEATURE 01.b:
        // See FF 01.a
        // Select check boxes next to user names in list when group is selected
        comboBoxGroups.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBoxGroups.getModel().getSize() > 1) {
                    int selectedIndex = comboBoxGroups.getSelectedIndex();
                    if (selectedIndex == 0) {
                        fieldGroupName.setText("");
                        fieldGroupDesc.setText("");
                    } else {
                        //TODO: Get the group Description in addition to GroupName - Will
                        String fieldText = "";
                        String fieldDesc = "";
                        for(Integer key : groups.keySet()) {
                            if(selectedIndex == key) {
                                fieldText = groups.get(key).get(0).toString();
                                fieldDesc = groups.get(key).get(1).toString();
                            }
                        }
                        fieldGroupName.setText(fieldText);
                        fieldGroupDesc.setText(fieldDesc);
                    }
                    fieldGroupName.setEnabled(true);
                    fieldGroupDesc.setEnabled(true);
                }
            }
        });

        /**
         * Listener for buttonSaveGroup
         * Checks that fieldGroupName has content
         * Does not require users to save a group
         * Adds users selected to the DB for that group
         */
        buttonSaveGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checkGroupName = false;
                boolean checkGroupDesc = false;

                // Check that the Group Name field has something in it and
                // set that text to the variable for DB update
                if(fieldGroupName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a Group Name");
                } else {
                    newGroupName = fieldGroupName.getText();
                    checkGroupName = true;
                }

                // Check that the Description field has something in it and
                // set that text to the variable for DB update
                if(fieldGroupDesc.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a description for the Group");
                } else {
                    newGroupDesc = fieldGroupDesc.getText();
                    checkGroupDesc = true;
                }

                // Make sure the group has both fields filled in and
                // call methods to submit changes to DB
                if(checkGroupName && checkGroupDesc) {
                    if(radioCreateNew.isSelected()) {
                        //TODO: FUTURE FEATURE 02: Add functionality to include users in group (different table USER_GROUP)
                        subs.logNewGroup(newGroupName, newGroupDesc);
                        // Add the ghost user to the new group automatically, regardless of user selection
                        subs.addUserGroup(ghostUserID, subs.getLastGroupID());
                        resetManageUserGroups();
                        JOptionPane.showMessageDialog(null, "Group Created");
                    }
                    else if (radioEditExisting.isSelected()) {
                        //TODO: FUTURE FEATURE 03: Add functionality to update users in group (different table USER_GROUP)
                        existingGroupID = comboBoxGroups.getSelectedIndex();
                        subs.updateExistingGroup(existingGroupID, newGroupName, newGroupDesc);
                        resetManageUserGroups();
                        JOptionPane.showMessageDialog(null, "Group Updated");
                    }
                }
            }
        });
    }

    /**
     * Model for the first drop down menu to select existing users found in DB.
     */
    private void populateUsers() {
        //TODO: Sort list alphabetically
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
        for(Role role : roles) {
            modelRoles.addElement(role.getRoleName());
        }
        comboBoxRoles.setModel(modelRoles);
    }

    /**
     * Model for the third drop down menu to select existing groups found in DB.
     */
    private void populateGroups() {
        modelGroups.removeAllElements();
        modelGroups.addElement("Choose one...");
        for(Integer key : groups.keySet()) {
            modelGroups.addElement( groups.get(key).get(0));
        }
        comboBoxGroups.setModel(modelGroups);
    }

    /**
     * Model for the user scroll list to select multiple users
     */
    private void populateSubscribers() {
        //TODO: Sort list alphabetically
        //TODO: FUTURE FEATURE 01.a:
        // Add Check Boxes next to users to indicate if they're in the selected group or not
        this.listUsers = new JList();
        DefaultListModel model = new DefaultListModel();
        model.removeAllElements();
        for(Recipient recipient : recipients) {
            model.addElement(recipient.getUserName());
        }
        listUsers.setModel(model);
        scrollPaneUsers.setViewportView(listUsers);
    }

    /**
     * Reset the fields and drop downs in the Manage User Roles section
     * For use in the Submit button listener
     */
    private void resetManageUserRoles() {
        labelCurrentRole.setText("None");
        comboBoxRoles.setSelectedIndex(0);
        checkBoxConfirm.setSelected(false);
        //checkBoxConfirm.setVisible(false);
        refreshComboBoxUsers();
        populateUsers();
    }

    /**
     * Clear the recipients (users) list in the Manage User Roles section
     * and repopulate from DB
     */
    private void refreshComboBoxUsers() {
        recipients.clear();
        recipients = subs.readSubscriberData();
    }

    /**
     * Reset the fields and drop downs in the Manage User Groups section
     * For use in the Save button listener
     */
    private void resetManageUserGroups() {
        comboBoxGroups.setSelectedIndex(0);
        fieldGroupName.setText("");
        fieldGroupName.setEnabled(false);
        fieldGroupDesc.setText("");
        fieldGroupDesc.setEnabled(false);
        refreshComboBoxGroups();
        populateGroups();
    }

    /**
     * Clear the groups list in the Manage User Groups section
     * and repopulate from DB
     */
    public void refreshComboBoxGroups() {
        groups.clear();
        groups = subs.getGroupMakeup();
    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
