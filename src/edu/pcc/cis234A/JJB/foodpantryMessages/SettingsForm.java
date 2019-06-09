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
    private JButton upateMyUserSettingsButton;
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
         * Action Listener for the top level notification settings check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        notificationOnOffCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(notificationOnOffCheckBox.isSelected()) {
                    setEnabledFieldStatus(true);
                    jjb.updateNotificationsOn(true);
                } else {
                    setEnabledFieldStatus(false);
                    jjb.updateNotificationsOn(false);
                }
            }
        });

        /**
         * Action Listener for the Cascade check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        cascadeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(cascadeCheckBox.isSelected()) {
                    jjb.updateCascadeOn(true);
                } else {
                    jjb.updateCascadeOn(false);
                }
            }
        });

        /**
         * Action Listener for the Rock Creek check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        rockCreekCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(rockCreekCheckBox.isSelected()) {
                    jjb.updateRockCreekOn(true);
                } else {
                    jjb.updateRockCreekOn(false);
                }
            }
        });

        /**
         * Action Listener for the Southeast check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        southeastCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(southeastCheckBox.isSelected()) {
                    jjb.updateSoutheastOn(true);
                } else {
                    jjb.updateSoutheastOn(false);
                }
            }
        });

        /**
         * Action Listener for the Sylvania check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        sylvaniaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(sylvaniaCheckBox.isSelected()) {
                    jjb.updateSylvaniaOn(true);
                } else {
                    jjb.updateSylvaniaOn(false);
                }
            }
        });

        /**
         * Action Listener for the Email check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        emailCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(emailCheckBox.isSelected()) {
                    jjb.updateEmailOn(true);
                } else {
                    jjb.updateEmailOn(false);
                }
            }
        });

        /**
         * Action Listener for the Alternate Email check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        alternateEmailCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(alternateEmailCheckBox.isSelected()) {
                    jjb.updateAltEmailOn(true);
                } else {
                    jjb.updateAltEmailOn(false);
                }
            }
        });

        /**
         * Action Listener for the SMS check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        smsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(smsCheckBox.isSelected()) {
                    jjb.updateSmsOn(true);
                } else {
                    jjb.updateSmsOn(false);
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
