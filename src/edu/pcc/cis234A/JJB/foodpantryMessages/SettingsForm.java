package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * The Settings form class
 *
 * @author Liana Schweitzer
 * @version 2019.06.10
 *
 * Spring 2 Part 2 Modifications:
 * - Removed functionality to set subscription settings out of constructor and created a new method.
 * - Removed functionality to set user settings out of constructor and created a new method.
 * - Added method to validate alternate email address
 * - Added method to parse phone number
 * - Added method to validate phone number
 * - Added button and action listener for updating user settings.
 * - Added logic to update user settings button to handle validation of alternate email address and phone number values
 * provided by the user.
 * - Removed functionality to retrieve the logged in user's username.
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
    private UserSetting userSetting;
    private JCheckBox[] boxes;

    /**
     * Creates the Food Pantry Settings form with notification and user settings.  Includes a method call to
     * initialize this tab with the user's settings from the database.  
     */
    public SettingsForm() {
        JavaneseJumpingBeansDB jjb = new JavaneseJumpingBeansDB();
        SubscriptionSetting subscriptionSettings;
        boxes = new JCheckBox[]{ cascadeCheckBox, rockCreekCheckBox, southeastCheckBox, sylvaniaCheckBox,
            emailCheckBox, alternateEmailCheckBox, smsCheckBox };
        emailTextField.setMargin(new Insets(0,2,2,0));
        altEmailTextField.setMargin(new Insets(0,2,2,0));
        phoneNumberTextField.setMargin(new Insets(0,2,2,0));

        subscriptionSettings = jjb.readSubscriptionSettings();
        if (subscriptionSettings.isNotificationsOn()) {
            setEnabledFieldStatus(true);
        } else {
            setEnabledFieldStatus(false);
        }
        setSubscriptionSettings(subscriptionSettings);

        userSetting = jjb.readUserSettings();
        setUserSettings(userSetting);

        /**
         * Action Listener for the top level notification settings check box. This is selected by default when a subscriber
         * first signs up for food pantry notifications.
         */
        notificationOnOffCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (notificationOnOffCheckBox.isSelected()) {
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
                if (cascadeCheckBox.isSelected()) {
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
                if (rockCreekCheckBox.isSelected()) {
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
                if (southeastCheckBox.isSelected()) {
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
                if (sylvaniaCheckBox.isSelected()) {
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
                if (emailCheckBox.isSelected()) {
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
                userSetting = jjb.readUserSettings();
                if (userSetting.getAltEmail().isEmpty()) {
                    if (alternateEmailCheckBox.isSelected()) {

                        alternateEmailCheckBox.setSelected(false);
                        JOptionPane.showMessageDialog(null, "You must add an alternate " +
                                "email address in user settings first.");
                    } else {
                        jjb.updateAltEmailOn(false);
                    }
                } else {
                    if (alternateEmailCheckBox.isSelected()) {
                        jjb.updateAltEmailOn(true);
                    } else {
                        jjb.updateAltEmailOn(false);
                    }
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
                userSetting = jjb.readUserSettings();
                if (userSetting.getPhoneNbr().isEmpty()) {
                    if(smsCheckBox.isSelected()) {
                        smsCheckBox.setSelected(false);
                        JOptionPane.showMessageDialog(null, "You must add a phone number " +
                                "in user settings first.");
                    } else {
                        jjb.updateSmsOn(false);
                    }
                } else {
                    if(smsCheckBox.isSelected()) {
                        jjb.updateSmsOn(true);
                    } else {
                        jjb.updateSmsOn(false);
                    }
                }
            }
        });

        /**
         * Action Listener for the Update My User Settings button. If the update is successful, then a message will pop
         * up to notify the user of this.  Otherwise if there is an error, a message will pop up to notify them of the
         * issue.
         */
        upateMyUserSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                userSetting = jjb.readUserSettings();
                String email = userSetting.getEmail();
                String dbAltEmail = userSetting.getAltEmail();
                String altEmail = altEmailTextField.getText().trim();
                String dbPhone = userSetting.getPhoneNbr();
                String phone = "";
                if (!phoneNumberTextField.getText().trim().isEmpty()) {
                    phone = parsePhoneNbr(phoneNumberTextField.getText().trim());
                }

                if (email.equals(altEmail)) {
                    JOptionPane.showMessageDialog(null, "Update failed!  Your email " +
                            "address and alternate email address cannot be the same.");
                } else if (dbAltEmail.equals(altEmail) && dbPhone.equals(phone)) {
                    if (dbAltEmail.isEmpty() && dbPhone.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Update failed!  You must enter " +
                                "values to update your user settings.");
                    } else if (!dbAltEmail.isEmpty() && !dbPhone.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Update failed!  Your alternate " +
                                "email address and phone number cannot match your existing ones.");
                        userSetting = jjb.readUserSettings();
                        setUserSettings(userSetting);
                    } else if (!dbAltEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Update failed!  Your alternate " +
                                "email address cannot match your existing one.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed!  Your phone " +
                                "number cannot match your existing one.");
                    }
                } else {
                    if (altEmail.isEmpty() && phone.isEmpty()) {
                        jjb.updateUserSettings(altEmail, phone);
                        JOptionPane.showMessageDialog(null, "Your user settings have been " +
                                "updated successfully!");
                        userSetting = jjb.readUserSettings();
                        setUserSettings(userSetting);
                    } else if (!altEmail.isEmpty() && !phone.isEmpty()) {
                        if (altEmailIsValid(altEmail) && validatePhoneNbr(phone)) { // && phone is valid
                            jjb.updateUserSettings(altEmail, phone);
                            JOptionPane.showMessageDialog(null, "Your user settings have been " +
                                    "updated successfully!");
                            userSetting = jjb.readUserSettings();
                            setUserSettings(userSetting);
                        } else if (validatePhoneNbr(phone)){
                            JOptionPane.showMessageDialog(null, "The alternate email address " +
                                    "you have entered is invalid!");
                        } else if (altEmailIsValid(altEmail)) {
                            JOptionPane.showMessageDialog(null, "The phone number " +
                                    "you have entered is invalid!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Your alternate email address " +
                                    "and phone number are invalid!");
                        }
                    } else if (altEmail.isEmpty()) {
                        if (!dbAltEmail.isEmpty()) {
                            if (validatePhoneNbr(phone)) {
                                jjb.updateUserSettings(altEmail, phone);
                                JOptionPane.showMessageDialog(null, "Your user settings have been " +
                                        "updated successfully!");
                                userSetting = jjb.readUserSettings();
                                setUserSettings(userSetting);
                            } else {
                                JOptionPane.showMessageDialog(null, "The phone number " +
                                        "you have entered is invalid!");
                            }
                        } else if (validatePhoneNbr(phone)) {
                            jjb.updateUserSettings(altEmail, phone);
                            JOptionPane.showMessageDialog(null, "Your phone number has been " +
                                    "updated successfully!");
                            userSetting = jjb.readUserSettings();
                            setUserSettings(userSetting);
                        } else {
                            JOptionPane.showMessageDialog(null, "The phone number " +
                                    "you have entered is invalid!");
                        }
                    } else {
                        if (!dbPhone.isEmpty()) {
                            if (altEmailIsValid(altEmail)) {
                                jjb.updateUserSettings(altEmail, phone);
                                JOptionPane.showMessageDialog(null, "Your user settings " +
                                        "have been updated successfully!");
                            } else {
                                JOptionPane.showMessageDialog(null, "The alternate email " +
                                        "address you have entered is invalid!");
                            }
                        } else if (altEmailIsValid(altEmail)) {
                            jjb.updateUserSettings(altEmail, phone);
                            JOptionPane.showMessageDialog(null, "Your alternate email " +
                                    "address has been updated successfully!");
                            userSetting = jjb.readUserSettings();
                            setUserSettings(userSetting);
                        } else {
                            JOptionPane.showMessageDialog(null, "Update failed!  Your " +
                                    "alternate email address is invalid.");
                        }
                    }
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


    private void setEnabledFieldStatus(Boolean status) {
        for (JCheckBox box : boxes) {
            box.setEnabled(status);
        }
        campusesLabel.setEnabled(status);
        notificationMethodsLabel.setEnabled(status);
    }

    private void setSubscriptionSettings(SubscriptionSetting subSettings) {
        notificationOnOffCheckBox.setSelected(subSettings.isNotificationsOn());
        cascadeCheckBox.setSelected(subSettings.isCascadeOn());
        rockCreekCheckBox.setSelected(subSettings.isRockCreekOn());
        southeastCheckBox.setSelected(subSettings.isSoutheastOn());
        sylvaniaCheckBox.setSelected(subSettings.isSylvaniaOn());
        emailCheckBox.setSelected(subSettings.isEmailOn());
        alternateEmailCheckBox.setSelected(subSettings.isAltEmailOn());
        smsCheckBox.setSelected(subSettings.isSmsOn());
    }

    private void setUserSettings(UserSetting usrSettings) {
        emailTextField.setText(usrSettings.getEmail());
        altEmailTextField.setText(usrSettings.getAltEmail());
        String phone = usrSettings.getPhoneNbr().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        phoneNumberTextField.setText(phone);
    }

    private static boolean altEmailIsValid(String altEmail)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (altEmail == null) {
            return false;
        }
        return pattern.matcher(altEmail).matches();
    }

    private boolean validatePhoneNbr(String phoneNbr) {
        for (char c : phoneNbr.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }

        return phoneNbr.length() == 10;
    }

    private String parsePhoneNbr(String phone) {
        StringBuilder phoneNbr = new StringBuilder();

        Iterable<String> phoneSplit = Splitter.on(CharMatcher.anyOf("-)("))
                .omitEmptyStrings().trimResults().split(phone);
        for (String p : phoneSplit) {
            phoneNbr.append(p);
        }
        return phoneNbr.toString();
    }

}
