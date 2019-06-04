package edu.pcc.cis234A.JJB.foodpantryMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Settings form class
 *
 * @author Liana Schweitzer
 * @version 2019.05.31
 *
 */
public class SettingsForm {
    private JPanel rootPanel;
    private JCheckBox notificationOnOffCheckBox;
    private JCheckBox cascadeCheckBox;
    private JCheckBox rockCreekCheckBox;
    private JCheckBox southeastCheckBox;
    private JCheckBox sylvaniaCheckBox;
    private JLabel subscriptionSettingsLabel;
    private JLabel campusesLabel;
    private JLabel notificationMethodsLabel;
    private JCheckBox emailCheckBox;
    private JCheckBox alternateEmailCheckBox;
    private JCheckBox smsCheckBox;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel altEmailLabel;
    private JTextField altEmailTextField;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumberTextField;
    private JCheckBox[] boxes;

    /**
     * Creates the Food Pantry Settings form with notification and user settings.  Includes a method call to
     * initialize this tab with the user's settings from the database.  
     */
    public SettingsForm() {
        UserLogin ul = new UserLogin();
        String un = ul.getLoggedInUser();
        JavaneseJumpingBeansDB jjb = new JavaneseJumpingBeansDB();
        SubscriptionSetting subscriptionSettings;
        UserSetting userSettings;
        boxes = new JCheckBox[]{ cascadeCheckBox, rockCreekCheckBox, southeastCheckBox, sylvaniaCheckBox,
            emailCheckBox, alternateEmailCheckBox, smsCheckBox };
        emailTextField.setMargin(new Insets(0,2,2,0));
        altEmailTextField.setMargin(new Insets(0,2,2,0));
        phoneNumberTextField.setMargin(new Insets(0,2,2,0));

        subscriptionSettings = jjb.readSubscriptionSettings(un);
        if(subscriptionSettings.isNotificationsOn()) {
            setEnabledFieldStatus(true);
        } else {
            setEnabledFieldStatus(false);
        }
        notificationOnOffCheckBox.setSelected(subscriptionSettings.isNotificationsOn());
        cascadeCheckBox.setSelected(subscriptionSettings.isCascadeOn());
        rockCreekCheckBox.setSelected(subscriptionSettings.isRockCreekOn());
        southeastCheckBox.setSelected(subscriptionSettings.isSoutheastOn());
        sylvaniaCheckBox.setSelected(subscriptionSettings.isSylvaniaOn());
        emailCheckBox.setSelected(subscriptionSettings.isEmailOn());
        alternateEmailCheckBox.setSelected(subscriptionSettings.isAltEmailOn());
        smsCheckBox.setSelected(subscriptionSettings.isSmsOn());

        userSettings = jjb.readUserSettings();
        emailTextField.setText(userSettings.getEmail());
        altEmailTextField.setText(userSettings.getAltEmail());
        String phone = userSettings.getPhoneNbr().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        phoneNumberTextField.setText(phone);

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
                    setEnabledFieldStatus(true);
                } else {
                    setEnabledFieldStatus(false);
                }
            }
        });
    }

    /**
     * Returns the root panel associated with the form.
     * @return the root panel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }


    public void setEnabledFieldStatus(Boolean status) {
        for (JCheckBox box : boxes) {
            box.setEnabled(status);
        }
        campusesLabel.setEnabled(status);
        notificationMethodsLabel.setEnabled(status);
    }
}
