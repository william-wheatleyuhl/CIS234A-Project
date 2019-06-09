package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * A class to define the functionality of the Manage Roles GUI
 * Allows a Manager to
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
    private SubscriberDB subs = new SubscriberDB();
    private ArrayList<Recipient> subscribers = subs.readSubscriberData();

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

        rootPanel.setPreferredSize(new Dimension(800, 600));

        comboBoxUsers.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBoxUsers.getModel().getSize() > 1) {
                    int selectedIndex = comboBoxUsers.getSelectedIndex();
                    if (selectedIndex == 0) {
                        comboBoxRoles.setEnabled(false);
                    } else {
                        comboBoxRoles.setEnabled(true);
                        //TODO: Populate with user's role
                        labelCurrentRole.setText("Placeholder for user's current role");
                    }
                }
            }
        });

        comboBoxRoles.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBoxRoles.getModel().getSize() > 1) {
                    int selectedIndex = comboBoxRoles.getSelectedIndex();
                    if (selectedIndex > 0) {
                        checkBoxConfirm.setVisible(true);
                        checkBoxConfirm.setEnabled(true);
                        checkBoxConfirm.setText("Check this box to confirm you want to change this user's role");
                    }
                }
            }
        });

        //TODO: Add listener for buttonSubmit (change role)
        //TODO: Add listener for radio Create New
        //TODO: Add listener for radio Edit Existing
        //TODO: Add listener for comboBoxGroups selection
        //TODO: Add listener for buttonSave (save group)
    }

    /**
     * Model for the first drop down menu to select existing users found in DB.
     */
    private void populateUsers() {
        modelUsers.removeAllElements();
        modelUsers.addElement("Choose one...");
        for(Recipient recipient : subscribers) {
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

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
