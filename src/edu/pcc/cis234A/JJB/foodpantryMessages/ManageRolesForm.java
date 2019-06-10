package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class to define the functionality of the Manage Roles GUI
 * Allows a Manager to change user roles and edit or create
 * user groups (subscriber lists)
 *
 * @author Syn
 * @version 2019.06.08
 *
 * Changelog:
 * 20190609 WWU - Implemented working roles class
 * 20190603 WWU - Populate "Current Role" label dynamically
 * 20190603 WWU - Finished Submit button for changing subscriber roles.
 * 20190603 WWU - comboBoxGroups now populates, as does the fieldGroupName textField
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
    private ArrayList<Role> roles = subs.readRoles(); //TODO: - Will - DONE
    private HashMap<Integer, ArrayList<Object>> groups = subs.getGroupMakeup();


    private int roleID;
    private int userID;
    private String newGroupName;

    DefaultComboBoxModel modelUsers = (DefaultComboBoxModel) comboBoxUsers.getModel();
    DefaultComboBoxModel modelRoles = (DefaultComboBoxModel) comboBoxRoles.getModel();
    DefaultComboBoxModel modelGroups = (DefaultComboBoxModel) comboBoxGroups.getModel();

    public ManageRolesForm() {
        populateUsers();
        populateRoles();
        populateGroups();
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
                        //TODO: Populate with user's current role - DONE
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
                    userID = comboBoxUsers.getSelectedIndex();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a user to change role");
                }

                //Check that a role is selected for the user
                if(comboBoxRoles.getSelectedIndex() > 0) {
                    checkUserRole = true;
                    //TODO: Add user role variable - DONE
                    roleID = comboBoxRoles.getSelectedIndex();
                    System.out.println(roleID);
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
                    //TODO: Update DB with new user role (get the user ID & Role ID selected in combo boxes) - DONE
                    subs.updateUserRole(roleID, userID);
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
                }
            }
        });

        /**
         * Listener for comboBoxGroups selection
         * Enables fieldGroupName and scrollPaneUsers
         * Populates fieldGroupName with the group name from DB for selected index
         */
        comboBoxGroups.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBoxGroups.getModel().getSize() > 1) {
                    int selectedIndex = comboBoxGroups.getSelectedIndex();
                    if (selectedIndex == 0) {
                        fieldGroupName.setText("");
                    } else {
                        //TODO: Need groups and groupName (Group class? like Template class?) - DONE
                        String fieldText = "";
                        for(Integer key : groups.keySet()) {
                            if(selectedIndex == key) {
                                fieldText = groups.get(key).get(0).toString();
                            }
                        }
                        fieldGroupName.setText(fieldText);
                    }
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

                if(fieldGroupName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a Group Name");
                } else {
                    newGroupName = fieldGroupName.getText();
                    checkGroupName = true;
                }

                if(checkGroupName) {
                    if(radioCreateNew.isSelected()) {
                        //TODO: Save new Group details to DB
                        JOptionPane.showMessageDialog(null, "Group Created");
                    }
                    else if (radioEditExisting.isSelected()) {
                        //TODO: Update group details in DB
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
        //TODO: Will - Change from manual entry to pull from DB (add getRole() method to SubscriberDB?) - DONE
        for(Role role : roles) {
            modelRoles.addElement(role.getRoleName());
        }
//        modelRoles.addElement("Subscriber");
//        modelRoles.addElement("Staff");
//        modelRoles.addElement("Manager");
        comboBoxRoles.setModel(modelRoles);
    }

    /**
     * Model for the third drop down menu to select existing groups found in DB.
     */
    private void populateGroups() {
        modelGroups.removeAllElements();
        modelGroups.addElement("Choose one...");
        //TODO: Add for loop to get groups - DONE
        for(Integer key : groups.keySet()) {
            modelGroups.addElement( groups.get(key).get(0));
        }
        comboBoxGroups.setModel(modelGroups);
    }

    private void populateSubscribers() {
        //TODO: populate multi-select list (scrollPaneUsers) with all users
    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
