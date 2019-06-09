package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;

/**
 * @author Syn
 * @version 2019.06.08
 */
public class ManageRolesForm {
    private JPanel rootPanel;
    private JLabel labelUsers;
    private JComboBox comboBoxRoles;
    private JComboBox comboBoxUsers;
    private JLabel labelRole;
    private JButton buttonSubmit;
    private JCheckBox checkBoxConfirm;
    private JLabel labelCurrentRoleText;
    private JLabel labelCurrentRole;

    DefaultComboBoxModel modelUsers = (DefaultComboBoxModel) comboBoxUsers.getModel();
    DefaultComboBoxModel modelRoles = (DefaultComboBoxModel) comboBoxRoles.getModel();

    public ManageRolesForm() {
        //populateUsers();
        //comboBoxRoles.setEnabled(false);
        //checkBoxConfirm.setEnabled(false);

        rootPanel.setPreferredSize(new Dimension(800, 600));


    }

//    private void populateUsers() {
//        modelUsers.removeAllElements();
//        modelRoles.addElement("Users:");
//        for(Template temp : templates) {
//            modelUsers.addElement(temp.userID);
//        }
//    }

    /**
     * @return rootPanel the root panel for the GUI
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
