package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Settings form class
 *
 * @author Liana Schweitzer
 * @version 2019.05.31
 *
 */
public class SettingsForm {
    private JPanel rootPanel;
    private JTable notificationDataTable;
    private JLabel minDateLabel;
    private JPanel minDatePanel;
    private JLabel maxDateLabel;
    private JLabel campusesLabel;
    private JCheckBox notificationOnOffCheckBox;
    private JCheckBox cascadeCheckBox;
    private JCheckBox rockCreekCheckBox;
    private JCheckBox southeastCheckBox;
    private JCheckBox sylvaniaCheckBox;
    private JLabel subscriptionSettingsLabel;
    private JLabel notificationMethodsLabel;
    private JCheckBox emailCheckBox;
    private JCheckBox alternateEmailCheckBox;
    private JCheckBox smsCheckBox;
    private JTextField emailTextField;
    private JLabel altEmailLabel;
    private JLabel emailLabel;
    private JTextField altEmailTextField;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumberTextField;
    JCheckBox[] boxes;
    private int id;

    /**
     * Creates the Food Pantry Settings form with notification and user settings.  Includes a method call to
     * initialize this tab with the user's settings from the database.  
     */
    public SettingsForm() {
        boxes = new JCheckBox[]{ cascadeCheckBox, rockCreekCheckBox, southeastCheckBox, sylvaniaCheckBox,
            emailCheckBox, alternateEmailCheckBox, smsCheckBox };
        // TODO: Replace below with loadSettings() method
        notificationOnOffCheckBox.setSelected(true);
        cascadeCheckBox.setSelected(true);
        rockCreekCheckBox.setSelected(true);
        southeastCheckBox.setSelected(true);
        sylvaniaCheckBox.setSelected(true);
        emailCheckBox.setSelected(true);
        alternateEmailCheckBox.setSelected(true);
        smsCheckBox.setSelected(true);

        /**
         * Action Listener for top level notification settings check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         * with the usernames from the Database. Deselects the "Specific Recipients" radio, and disables the Recipients
         * field from editing.
         */
        notificationOnOffCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(notificationOnOffCheckBox.isSelected()) {
                    for (JCheckBox box : boxes) {
                        box.setEnabled(true);
                    }
                } else {
                    for (JCheckBox box : boxes) {
                        box.setEnabled(false);
                    }
                }
            }
        });
        /**
         * Action Listener for the "Specific Users" radio button. Disables the "All Users" radio, sets text of Recipient
         * List to an empty text field.
         * TODO: Parse Recipient field to detect usernames.
         */
        /*specificRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(specificRadio.isSelected()) {
                    groupID = groupSelect.getSelectedIndex();
                    groupSelect.setEnabled(true);
                    allRadio.setSelected(false);
                }
            }
        });*/
    }

    /**
     * Returns the root panel associated with the form.
     * @return the root panel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }


}
