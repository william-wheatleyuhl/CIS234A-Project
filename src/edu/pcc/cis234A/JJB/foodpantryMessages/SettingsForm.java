package edu.pcc.cis234A.JJB.foodpantryMessages;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.twilio.rest.chat.v1.service.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static java.awt.Label.CENTER;

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
    private UserSetting userSetting;
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
        setSubscriptionSettings(subscriptionSettings);

        userSetting = jjb.readUserSettings();
        setUserSettings(userSetting);

        UserLogin userLogin = new UserLogin();

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
                String altEmailNoTrim = altEmailTextField.getText();
                String altEmail = altEmailTextField.getText().trim();
                String dbPhone = userSetting.getPhoneNbr();
                String phone = phoneNumberTextField.getText().trim();
                /*if (validatePhoneNbr(phoneNumberTextField.getText().trim())) {
                    phone = parsePhoneNbr(phoneNumberTextField.getText().trim());
                    System.out.println(phone);
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed!  Please enter a " +
                            "valid phone number.  E.g. (503) 777-7777.");
                    return;
                }*/

                System.out.println("DB Alt Email: " + dbAltEmail + " || Text field Alt Email: " +
                        altEmailNoTrim + " || DB Phone Nbr: " + dbPhone + " || Text field Phone Nbr: " + phone);
                // email == alt email
                if (email.equals(altEmail)) {
                    JOptionPane.showMessageDialog(null, "Update failed!  Your email " +
                            "address and alternate email address cannot be the same.");
                    System.out.println("1x");
                    System.out.println("--------------------------------------------------------");
                // alt emails and phone numbers are equal
                } else if (dbAltEmail.equals(altEmail) && dbPhone.equals(phone)) {
                    System.out.println("2x");
                    // don't update db with null when db is already null
                    if (dbAltEmail.isEmpty() && dbPhone.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Update failed!  You must enter " +
                                "values to update your user settings.");
                        System.out.println("3x");
                        System.out.println("--------------------------------------------------------");
                    // don't update db if input is the same
                    } else if (!dbAltEmail.isEmpty() && !dbPhone.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Update failed!  Your alternate " +
                                "email address and phone number cannot match your existing ones.");
                        System.out.println("4x");
                        System.out.println("--------------------------------------------------------");
                    // update db with alt email
                    } else if (!dbAltEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Update failed!  Your alternate " +
                                "email address cannot match your existing one.");
                        System.out.println("5x");
                        System.out.println("--------------------------------------------------------");
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed!  Your phone " +
                                "number cannot match your existing one.");
                        System.out.println("6x");
                        System.out.println("--------------------------------------------------------");
                    }
                // alt email and/or phone db values don't match input
                } else {
                    System.out.println("7x");
                    // db values exist, input is empty
                    if (altEmail.isEmpty() && phone.isEmpty()) {
                        jjb.updateUserSettings(altEmail, phone);
                        JOptionPane.showMessageDialog(null, "Your user settings have been " +
                                "updated successfully!");
                        System.out.println("8x");
                        System.out.println("--------------------------------------------------------");
                    // db values don't exist, input is provided
                    } else if (!altEmail.isEmpty() && !phone.isEmpty()) {
                        System.out.println("9x");
                        if (altEmailIsValid(altEmail)) { // && phone is valid
                            jjb.updateUserSettings(altEmail, phone);
                            JOptionPane.showMessageDialog(null, "Your user settings have been " +
                                    "updated successfully!");
                            System.out.println("10x");
                            System.out.println("--------------------------------------------------------");
                        } else {
                            JOptionPane.showMessageDialog(null, "Your input is invalid!");
                            System.out.println("11x");
                            System.out.println("--------------------------------------------------------");
                        }
                    // db value exists for alt email
                    } else if (altEmail.isEmpty()) {
                        jjb.updateUserSettings(altEmail, phone);
                        JOptionPane.showMessageDialog(null, "Your phone number has been " +
                                "updated successfully!");

                        System.out.println("12x");
                        System.out.println("--------------------------------------------------------");
                    } else {
                        System.out.println("13x");
                        if (altEmailIsValid(altEmail)) {
                            jjb.updateUserSettings(altEmail, phone);
                            JOptionPane.showMessageDialog(null, "Your user settings have been " +
                                    "updated successfully!");
                            System.out.println("14x");
                            System.out.println("--------------------------------------------------------");
                        } else {
                            System.out.println("15x");
                            JOptionPane.showMessageDialog(null, "Update failed!  Your " +
                                    "alternate email address is invalid.");
                            System.out.println("--------------------------------------------------------");
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


    public void setEnabledFieldStatus(Boolean status) {
        for (JCheckBox box : boxes) {
            box.setEnabled(status);
        }
        campusesLabel.setEnabled(status);
        notificationMethodsLabel.setEnabled(status);
    }

    public void setSubscriptionSettings(SubscriptionSetting subSettings) {
        notificationOnOffCheckBox.setSelected(subSettings.isNotificationsOn());
        cascadeCheckBox.setSelected(subSettings.isCascadeOn());
        rockCreekCheckBox.setSelected(subSettings.isRockCreekOn());
        southeastCheckBox.setSelected(subSettings.isSoutheastOn());
        sylvaniaCheckBox.setSelected(subSettings.isSylvaniaOn());
        emailCheckBox.setSelected(subSettings.isEmailOn());
        alternateEmailCheckBox.setSelected(subSettings.isAltEmailOn());
        smsCheckBox.setSelected(subSettings.isSmsOn());
    }

    public void setUserSettings(UserSetting usrSettings) {
        emailTextField.setText(usrSettings.getEmail());
        altEmailTextField.setText(usrSettings.getAltEmail());
        String phone = usrSettings.getPhoneNbr().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        phoneNumberTextField.setText(phone);
    }

    public static boolean altEmailIsValid(String altEmail)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (altEmail == null)
            return false;
        return pattern.matcher(altEmail).matches();
    }

    public boolean validatePhoneNbr(String phoneNbr) {
        String regexStr = "^[0-9\\-]*$";
        if (phoneNbr.matches(regexStr)) {
            return true;
        } else {
            return false;
        }
    }

    public String parsePhoneNbr(String phone) {
        String phoneNbr = "";
        System.out.println("parsePhoneNbr phone passed in: " + phone);
        String formattedPhoneNumber = "(123) 456-7890";

        Iterable<String> phoneSplit = Splitter.on(CharMatcher.anyOf("-)("))
                .omitEmptyStrings().trimResults().split(formattedPhoneNumber);
        System.out.println("parsePhoneNbr phone passed in: " + phoneSplit.toString());
        for (String p : phoneSplit) {
            phoneNbr = phoneNbr + p;
        }
        System.out.println("parsePhoneNbr phoneNbr: " + phoneNbr);
        return phoneNbr;
    }

}
